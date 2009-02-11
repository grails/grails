package org.grails.plugin

import grails.test.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class PluginServiceUnitTests extends GrailsUnitTestCase {

    void testGenerateMasterPlugins() {
        def service = new PluginService()
        def pluginsListRetrieved

        URL.metaClass.constructor = { path ->
            [text:org.grails.plugin.xml.PluginsListXmlMock.PLUGINS_LIST]
        }
        
        def plugins = service.generateMasterPlugins()
        
        assertNotNull plugins
        assertEquals 3, plugins.size()
        assertEquals 'autobase', plugins[0].name
        assertEquals '0.8.1', plugins[0].currentRelease
        assertEquals 'avatar', plugins[1].name
        assertEquals '0.3', plugins[1].currentRelease
        assertEquals 'yui', plugins[2].name
        assertEquals '2.6.0', plugins[2].currentRelease
        
        def autobase = plugins[0]
        // focus on the rest of the attribs on one plugin
        assertEquals 'Automate your database work as much as possible', autobase.title
        assertEquals 'Robert Fischer', autobase.author
        assertEquals 'robert.fischer@smokejumperit.com', autobase.authorEmail
        assertTrue autobase.description.startsWith('This plugin marries the ')
        assertEquals 'http://github.com/RobertFischer/autobase/wikis', autobase.documentationUrl
        assertEquals 'http://plugins.grails.org/grails-autobase/tags/RELEASE_0_8_1/grails-autobase-0.8.1.zip', autobase.downloadUrl
    }
    
    void testTranslateMasterPlugins() {
        def master = []
        master << new Plugin(
            name: 'plugin-a',
            title: 'Plugin A Plugin',
            description: 'hosted at grails.org',
            author: 'Peter Jackson',
            authorEmail: 'peter@jackson.com',
            documentationUrl: 'http://www.grails.org/Plugin+A+Plugin',
            downloadUrl: 'http://www.somewhere.com/plugin-a-5.0.2.zip',
            currentRelease: '5.0.2'
        )
    }
}
