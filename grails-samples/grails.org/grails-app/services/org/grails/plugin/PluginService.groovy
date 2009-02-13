package org.grails.plugin

import org.codehaus.groovy.grails.commons.ConfigurationHolder

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
        
        log.info "Found ${plugins.plugin.size()} master plugins."
        
        plugins.plugin.inject([]) { pluginsList, pxml ->
            if (!pxml.release.size()) return pluginsList
            def latestRelease = pxml.release[pxml.release.size()-1]
            def p = new Plugin()
            p.with {
                name = pxml.@name
                title = latestRelease.title.toString() ?: pxml.@name
                description = latestRelease.description
                body = 'see desc'
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
        masters.each {
            def plugin = Plugin.findByName(it.name)
            // try by title
            if (!plugin) {
                plugin = Plugin.findByTitleLike(it.title)
            }
            // try by title matching name
            if (!plugin) {
                plugin = Plugin.findByNameIsNullAndTitleLike("%${it.name}%")
            }

            if (!plugin) {
                // save new master plugin
                if (!it.save()) {
                    log.error "Could not save master plugin: $it.name ($it.title), version $it.currentRelease"
                    it.errors.allErrors.each { log.error "\t$it" }
                } else {
                    log.info "New plugin was saved from master: $it.name"
                }
            } else {
                // update existing plugin
                updatePlugin(plugin, it)
            }
        }
    }

    def updatePlugin(plugin, master) {
        // these attributes are overriden by local plugin domain changes
        updatePluginAttribute('title', plugin, master)
        updatePluginAttribute('author', plugin, master)
        updatePluginAttribute('authorEmail', plugin, master)
        updatePluginAttribute('body', plugin, master)
        
        // these are always overridden by the master list
        plugin.name = master.name
        plugin.description = master.description
        plugin.documentationUrl = master.documentationUrl
        plugin.downloadUrl = master.downloadUrl
        plugin.currentRelease = master.currentRelease
        
        log.info "Local plugin '$plugin.name' was updated with master version."
    }
    
    def updatePluginAttribute(propName, plugin, master) {
        if (master."$propName" && !plugin."$propName") {
            plugin."$propName" = master."$propName"
        }
    }
}
