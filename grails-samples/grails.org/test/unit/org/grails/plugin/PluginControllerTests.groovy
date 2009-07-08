package org.grails.plugin

import grails.test.ControllerUnitTestCase
import org.grails.wiki.WikiPage
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/*
 * author: Matthew Taylor
 */
class PluginControllerTests extends ControllerUnitTestCase {
    def controller
    
    void setUp() {
        super.setUp()
        controller = new PluginController()
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

        def model = controller.show()

        assertEquals p, model.plugin
    }

    void testCreatePluginGET() {
        mockRequest.method = 'GET'
        mockParams.title='My Plugin'
        mockDomain(WikiPage)

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

        controller.createPlugin()

        assertEquals "show", redirectArgs.action
    }
    
    void testHomePopulatesFeaturedPluginsByDefault() {
        def plugins = [
            new Plugin(name:'not featured 1', featured:false),
            new Plugin(name:'featured 1', featured: true),
            new Plugin(name:'not featured 2', featured: false),
            new Plugin(name:'featured 2', featured: true)
        ]
        mockDomain(Plugin, plugins)
        controller.metaClass.getCommentService = { -> [getLatestComments: { String type, num -> []}]}
        def model = controller.home()
        
        assertNotNull model.currentPlugins
        assertEquals 2, model.currentPlugins.size()
        assertTrue model.currentPlugins*.name.contains('featured 1')
    }
    
    void testHomePopulatesAllPlugins() {
        def plugins = [
            new Plugin(name:'not featured 1', featured:false),
            new Plugin(name:'featured 1', featured: true),
            new Plugin(name:'not featured 2', featured: false),
            new Plugin(name:'featured 2', featured: true)
        ]
        mockDomain(Plugin, plugins)
        controller.metaClass.getParams = { -> [category:'all'] }
        controller.metaClass.getCommentService = { -> [getLatestComments: { String type, num-> []}]}
        def model = controller.home()
        
        assertNotNull model.currentPlugins
        assertEquals 4, model.currentPlugins.size()
    }
    
    void testHomePopulatesMostPopularPlugins() {
        def plugins = []
        Plugin.metaClass.getAverageRating = { ->
            delegate.id % 2 == 0 ? delegate.id * 10 : delegate.id // only evens get the big ratings
        }
        10.times { i ->
            plugins << new Plugin(name:"number $i", id: i)
        }
        mockDomain(Plugin, plugins)
        controller.metaClass.getParams = { -> [category:'popular'] }
        controller.metaClass.getCommentService = { -> [getLatestComments: { String type, num-> []}]}
        def model = controller.home()
        
        assertNotNull model.currentPlugins
        assertEquals 10, model.currentPlugins.size()
        // assert that every rating is the same or less than the previous (sorted by popularity)
        def cur
        model.currentPlugins.each {
            if (!cur) {
                cur = it
                return
            }
            println "asserting $it.averageRating <= $cur.averageRating"
            assertTrue it.averageRating <= cur.averageRating
        }
    }
    
    void testHomePopulatesRecentlyUpdatedPlugins() {
        def plugins = [
            new Plugin(name:'fourth', lastReleased: new GregorianCalendar(2001,01,01).time),
            new Plugin(name:'second', lastReleased: new GregorianCalendar(2008,01,01).time),
            new Plugin(name:'first', lastReleased: new GregorianCalendar(2009,01,01).time),
            new Plugin(name:'third', lastReleased: new GregorianCalendar(2007,01,01).time)
        ]
        mockDomain(Plugin, plugins)
        controller.metaClass.getParams = { -> [category:'recentlyUpdated'] }
        controller.metaClass.getCommentService = { -> [getLatestComments: { String type, num-> []}]}
        def model = controller.home()
        
        assertNotNull model.currentPlugins
        assertEquals 4, model.currentPlugins.size()
        assertEquals 'first', model.currentPlugins[0].name
        assertEquals 'second', model.currentPlugins[1].name
        assertEquals 'third', model.currentPlugins[2].name
        assertEquals 'fourth', model.currentPlugins[3].name
    }
    
    // test order
    
    // test max
    
    // test sort
    
    // test offset
}