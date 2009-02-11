package org.grails.plugin

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class PluginService {

    boolean transactional = true
    
    def grailsApplication
    
    void afterPropertiesSet() {}

    def generateMasterPlugins() {
        def pluginLoc = ConfigurationHolder.config?.plugins?.pluginslist
        def listFile = new URL(pluginLoc)
        def listText = listFile.text
        // remove the first line of <?xml blah/>
        listText = listText.replaceAll(/\<\?xml ([^\<\>]*)\>/, '')
        def plugins = new XmlSlurper().parseText(listText)
        
        def pluginsList = []
        
        plugins.plugin.each { pxml ->
            if (!pxml.release.size()) return
            def latestRelease = pxml.release[pxml.release.size()-1]
            pluginsList << new Plugin(
                name: pxml.@name,
                title: latestRelease.title,
                description: latestRelease.description,
                author: latestRelease.author,
                authorEmail: latestRelease.authorEmail,
                documentationUrl: latestRelease.documentation,
                downloadUrl: latestRelease.file,
                currentRelease: latestRelease.@version
            )
        }
        pluginsList
    }
}
