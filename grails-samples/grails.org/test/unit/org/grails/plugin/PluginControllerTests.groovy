package org.grails.plugin

import grails.test.ControllerUnitTestCase
import org.grails.wiki.WikiPage

/*
 * author: Matthew Taylor
 */
class PluginControllerTests extends ControllerUnitTestCase {

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
        mockParams.description = 'stuff here'

        def controller = new PluginController()
        def model = controller.createPlugin()

        assert model.plugin
        assertEquals 'My Plugin', model.plugin.title
        assertEquals 'stuff here', model.plugin.description.body
    }

    void testCreatePluginValidationError() {
        mockRequest.method = 'POST'
        mockParams.title='my plugin'
        mockParams.description='stuff here'

        Plugin.metaClass.save = { -> null }

        def controller = new PluginController()
        def model = controller.createPlugin()

        assert model.plugin
        assertEquals 'my plugin', model.plugin.title
    }

    void testCreatePluginValidationSuccess() {
        mockDomain(Plugin)
        mockRequest.method = 'POST'
        mockParams.title='my plugin'
        mockParams.description='stuff here'
        def redirectParams = [:]

        PluginController.metaClass.redirect = { Map args -> redirectParams = args }
        Plugin.metaClass.save = { -> delegate }

        def controller = new PluginController()
        controller.createPlugin()

        assertEquals "/", redirectParams.uri
    }
}