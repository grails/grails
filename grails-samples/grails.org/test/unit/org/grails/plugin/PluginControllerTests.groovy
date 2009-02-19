package org.grails.plugin

import grails.test.ControllerUnitTestCase
import org.grails.wiki.WikiPage
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/*
 * author: Matthew Taylor
 */
class PluginControllerTests extends ControllerUnitTestCase {
    def cache = [:]

    void setUp() {
        super.setUp()
//        cache.config = ConfigurationHolder.config
        ConfigurationHolder.config = [grails:[serverURL:'serverurl']]
    }

    void tearDown() {
        super.tearDown()
//        ConfigurationHolder.metaClass.static.getConfig = cache.configHolderGetConfig
    }

    void testShowPlugin() {
        Plugin p = new Plugin(name:'plugin', title:'My Plugin', comments:[])

        mockDomain(Plugin, [p])
        mockParams.name = 'plugin'

        def controller = new PluginController()
        def model = controller.show()

        assert p

        assertEquals p, model.plugin
    }

    void testCreatePluginGET() {
        mockRequest.method = 'GET'
        mockParams.title='My Plugin'
        mockDomain(WikiPage)

        def controller = new PluginController()
        def model = controller.createPlugin()

        assert model.plugin
        assertEquals 'My Plugin', model.plugin.title
    }

    void testCreatePluginValidationError() {
        mockRequest.method = 'POST'
        mockParams.title='my plugin'
        mockDomain(WikiPage)

        Plugin.metaClass.save = { -> null }

        def controller = new PluginController()
        def model = controller.createPlugin()

        assert model.plugin
        assertEquals 'my plugin', model.plugin.title
    }

    void testCreatePluginValidationSuccess() {
        mockDomain(Plugin)
        mockDomain(WikiPage)
        mockRequest.method = 'POST'
        mockParams.title='my plugin'
        mockParams.name='my-plugin'
        mockParams.authorEmail='blargh'
        mockParams.currentRelease='1.2'
        mockParams.downloadUrl='durl'
        def redirectParams = [:]

        def controller = new PluginController()
        controller.createPlugin()

        assertEquals "show", redirectArgs.action
    }
}