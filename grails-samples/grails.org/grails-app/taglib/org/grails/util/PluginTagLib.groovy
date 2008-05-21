package org.grails.util

import org.codehaus.groovy.grails.plugins.PluginManagerHolder

/**
 * @author Graeme Rocher
 * @since 1.0
 * 
 * Created: May 21, 2008
 */
class PluginTagLib {

    static namespace = "plugin"

    def isAvailable = { attrs, body ->
        def name = attrs.name
        def version = attrs.version

        if(name) {
            def pluginManager = PluginManagerHolder.getPluginManager()
            if(version && pluginManager.getGrailsPlugin(name, version)) {
                out << body()
            }
            else if(pluginManager.hasGrailsPlugin(name)) {
                out << body()                
            }
        }
    }
}