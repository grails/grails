package org.grails.plugin

import grails.test.ControllerUnitTestCase
import org.grails.wiki.WikiPage
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/*
 * author: Matthew Taylor
 */
class PluginControllerTests extends ControllerUnitTestCase {

    void setUp() {
        super.setUp()
        ConfigurationHolder.config = [grails:[serverURL:'serverurl']]
    }

    void tearDown() {
        super.tearDown()
    }

    void testShowPlugin() {
        Plugin p = new Plugin(name:'plugin', title:'My Plugin')

        mockDomain(Plugin, [p])
        mockParams.name = 'plugin'
        p.metaClass.getComments = {-> [] }

        def controller = new PluginController()
        def model = controller.show()

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
        Plugin.metaClass.save = { Map m -> true }

        def controller = new PluginController()
        controller.createPlugin()

        assertEquals "show", redirectArgs.action
    }
}