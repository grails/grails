package org.grails.plugin

class PluginServiceIntegrationTests extends GroovyTestCase {
    
    void testGenerateMasterPlugins() {
        def service = new PluginService()
        def plugins = service.generateMasterPlugins()
        
        assertNotNull plugins
        println plugins.size()
        println plugins
        assertTrue plugins.size() > 100
    }
    
}