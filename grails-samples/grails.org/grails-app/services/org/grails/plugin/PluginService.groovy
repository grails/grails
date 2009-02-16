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
        
        println "Found ${plugins.plugin.size()} master plugins."
        log.info "Found ${plugins.plugin.size()} master plugins."

        plugins.plugin.inject([]) { pluginsList, pxml ->
            if (!pxml.release.size()) return pluginsList
            def latestRelease = pxml.release[pxml.release.size()-1]
            def p = new Plugin()
            p.with {
                name = pxml.@name
                title = latestRelease.title.toString() ?: pxml.@name
                description = new WikiPage(title:'Description', body:latestRelease.description.toString() ?: 'Not provided')
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
        println "translating master plugins:"
        masters.each { master ->
            println "for ==> $master.name"
            def plugin = Plugin.findByName(master.name)
            println ".... found plugin: $plugin"
            // try by title
            if (!plugin) {
                plugin = Plugin.findByTitleLike(master.title)
            }
            // try by title matching name
            if (!plugin) {
                plugin = Plugin.findByNameLikeAndTitleLike('fix-this-%', "%${master.name}%")
            }

            if (!plugin) {
                // before saving the master, we need to save the description wiki page
                if (!master.description.save() && master.description.hasErrors()) {
                    master.description.errors.allErrors.each { println it }
                }
                // save new master plugin
                if (!master.save()) {
                    println   "Could not save master plugin: $master.name ($master.title), version $master.currentRelease"
                    log.error "Could not save master plugin: $master.name ($master.title), version $master.currentRelease"
                    master.errors.allErrors.each { log.error "\t$it"; println "\t$it" }
                } else {
                    println  "New plugin was saved from master: $master.name"
                    log.info "New plugin was saved from master: $master.name"
                }
            } else {
                // update existing plugin
                updatePlugin(plugin, master)
            }
        }
    }

    def updatePlugin(plugin, master) {
        println "Updating plugin \"$plugin.name\"..."
        // handle the wiki page with some care
        if (master.description?.body && !plugin.description?.body) {
            plugin.description = master.description
            if (!plugin.description.validate()) { plugin.description.errors.allErrors.each { println it } }
            assert plugin.description.save()
            Version v = plugin.description.createVersion()
            v.author = User.findByLogin('admin')
            if (!v.validate()) { v.errors.allErrors.each { println it } }
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
            println  "Local plugin '$plugin.name' was not updated properly... errors follow:"
            log.warn "Local plugin '$plugin.name' was not updated properly... errors follow:"
            plugin.errors.allErrors.each { log.warn it; println it }
        }
        
        println  "Local plugin '$plugin.name' was updated with master version."
        log.info "Local plugin '$plugin.name' was updated with master version."
    }
    
    def updatePluginAttribute(propName, plugin, master) {
        if (master."$propName" && !plugin."$propName") {
            plugin."$propName" = master."$propName"
        }
    }
}
