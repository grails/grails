package org.grails.plugin

class PluginServiceIntegrationTests extends GroovyTestCase {
    
    void testGenerateMasterPlugins() {
        def service = new PluginService()
        def plugins = service.generateMasterPlugins()
        
        assertNotNull plugins
        assertTrue plugins.size() > 100
        plugins.each {
            def valid = it.validate()
            if (it.hasErrors()) println it.errors
            assertTrue valid
        }
    }
    
}