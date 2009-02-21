package org.grails.plugin

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.wiki.WikiPage
import org.grails.auth.User
import org.grails.content.Version

class PluginService {

    boolean transactional = true
    
    def runMasterUpdate() {
        translateMasterPlugins(generateMasterPlugins())
    }
    
    def generateMasterPlugins() {
        def pluginLoc = ConfigurationHolder.config?.plugins?.pluginslist
        def listFile = new URL(pluginLoc)
        def listText = listFile.text
        // remove the first line of <?xml blah/>
        listText = listText.replaceAll(/\<\?xml ([^\<\>]*)\>/, '')
        def plugins = new XmlSlurper().parseText(listText)
        
//        println "Found ${plugins.plugin.size()} master plugins."
        log.info "Found ${plugins.plugin.size()} master plugins."

        plugins.plugin.inject([]) { pluginsList, pxml ->
            if (!pxml.release.size()) return pluginsList
//            println "processing xml node for ${pxml.@name}"
            def latestRelease = pxml.release[pxml.release.size()-1]
//            println "\tlatest release is ${latestRelease.title}:${latestRelease.@version}"
            def p = new Plugin()
            p.with {
                name = pxml.@name
                title = latestRelease.title.toString() ?: pxml.@name
                description = new WikiPage(body:latestRelease.description.toString() ?: '')
                author = latestRelease.author
                authorEmail = latestRelease.authorEmail
                documentationUrl = latestRelease.documentation
                downloadUrl = latestRelease.file
                currentRelease = latestRelease.@version
            }

            pluginsList << p
        }
    }

    def translateMasterPlugins(masters) {
//        println "translating master plugins:"
        masters.each { master ->
//            println "==> $master.name <== MASTER"
            def plugin = Plugin.findByName(master.name)
            // try by title
            if (!plugin) {
                plugin = Plugin.findByTitleLike(master.title)
                //except 'Functional Testing', which is unfortunately named
                if (master.title == 'Functional Testing') plugin = null
            }
            // try by title matching name
            if (!plugin) {
                plugin = Plugin.findByNameLikeAndTitleLike('fix-this-%', "%${master.name}%")
            }

            if (!plugin) {
                // injecting a unique wiki page name for description
                // pull off the desc so we don't try to save it
                def descWiki = master.description
                master.description = null
                // so we need to save the master first to get its id
                if (!master.save()) {
                    log.error "Could not save master plugin: $master.name ($master.title), version $master.currentRelease"
                    master.errors.allErrors.each { log.error "\t$it" }
                }
                // put the wiki page back with a unique title
                descWiki.title = "description-${master.id}"
                master.description = descWiki
//                println "No existing plugin, creating new ==> ${master.name}"
                // before saving the master, we need to save the description wiki page
                if (!master.description.save() && master.description.hasErrors()) {
                    master.description.errors.allErrors.each { log.error it }
                } else {
                    def v = master.description.createVersion()
                    v.author = User.findByLogin('admin')
                    try {
                        v.save(flush:true)
                    } catch (Exception e) {
                        println "WARNING: Can't save version ${v.title} (${v.number})"
                    }
                }
                //inject dummy wikis for users to fill in
                (Plugin.WIKIS - 'description').each { wiki ->
                    master."$wiki" = new WikiPage(title:"$wiki-${master.id}", body:'')
                    assert master."$wiki".save()
                }
                // save new master plugin
                if (!master.save()) {
//                    println   "Could not save master plugin: $master.name ($master.title), version $master.currentRelease"
                    log.error "Could not save master plugin: $master.name ($master.title), version $master.currentRelease"
                    master.errors.allErrors.each { log.error "\t$it" }
                } else {
//                    println  "New plugin was saved from master: $master.name"
                    log.info "New plugin was saved from master: $master.name"
                }
            } else {
                // update existing plugin
                updatePlugin(plugin, master)
            }
        }
    }

    def updatePlugin(plugin, master) {
//        println "Updating plugin \"$plugin.name\"..."
        // handle the wiki page with some care
        if (master.description?.body && !plugin.description?.body) {
            plugin.description = master.description
//            if (!plugin.description.validate()) { plugin.description.errors.allErrors.each { println it } }
            assert plugin.description.save()
            Version v = plugin.description.createVersion()
            v.author = User.findByLogin('admin')
//            if (!v.validate()) { v.errors.allErrors.each { println it } }
            assert v.save()
        }

        // these attributes are overriden by local plugin domain changes
        updatePluginAttribute('title', plugin, master)
        updatePluginAttribute('author', plugin, master)
        updatePluginAttribute('authorEmail', plugin, master)
        
        // these are always overridden by the master list
        plugin.name = master.name
        plugin.documentationUrl = master.documentationUrl
        plugin.downloadUrl = master.downloadUrl
        plugin.currentRelease = master.currentRelease

        if (!plugin.save()) {
//            println  "Local plugin '$plugin.name' was not updated properly... errors follow:"
            log.warn "Local plugin '$plugin.name' was not updated properly... errors follow:"
//            plugin.errors.allErrors.each { log.warn it; println it }
        } else {
            def v = plugin.description.createVersion()
            v.author = User.findByLogin('admin')
            try {
                assert v.save(flush:true)
            } catch (Exception e) {
                println "WARNING: Can't save version ${v.title} (${v.number})"
            }
        }
        
//        println  "Local plugin '$plugin.name' was updated with master version."
        log.info "Local plugin '$plugin.name' was updated with master version."
    }
    
    def updatePluginAttribute(propName, plugin, master) {
        if (master."$propName" && !plugin."$propName") {
            plugin."$propName" = master."$propName"
        }
    }
}
