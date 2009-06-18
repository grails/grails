package org.grails

import org.grails.news.NewsItem
import org.grails.ContentController
import org.grails.blog.BlogEntry
import org.grails.cache.CacheService
import net.sf.ehcache.Cache
import net.sf.ehcache.CacheManager
import org.grails.content.Version
import org.grails.content.Content
import org.codehaus.groovy.grails.plugins.codecs.URLCodec
import org.grails.wiki.*
import org.grails.plugin.PluginService

/**
 * @author Graeme Rocher
 * @since 1.0
 *
 * Created: Feb 28, 2008
 */
class ContentControllerTests extends grails.test.ControllerUnitTestCase {

    protected void setUp() {
        super.setUp();

        String.metaClass.decodeURL = { URLCodec.decode delegate }
        String.metaClass.encodeAsURL = { URLCodec.encode delegate }
    }

    protected void tearDown() {
        def remove = GroovySystem.metaClassRegistry.&removeMetaClass
        remove ContentController
        remove NewsItem
        remove WikiPage
        remove BlogEntry
        remove Content
        remove Version
        remove ContentController
    }

    void testIndexNoId() {
        mockDomain(BlogEntry)
        ContentController.metaClass.getRequest = {-> [method: "POST"] }
        ContentController.metaClass.getParams = {-> [id: null] }

        def renderParams = [:]

        def controller = new ContentController()
        controller.metaClass.getPluginService = { -> [newestPlugins: { num -> [] } ] }
        controller.metaClass.getScreencastService = { -> [getLatestScreencastId: { -> 5 } ] }
        controller.metaClass.render = {Map args -> renderParams = args }

        controller.index()

        assertEquals "homePage", renderParams.view

    }

    void testIndexHomeId() {
        mockDomain(BlogEntry)
        ContentController.metaClass.getRequest = {-> [method: "POST"] }
        ContentController.metaClass.getParams = {-> [id: "Home"] }

        def renderParams = [:]
        
        def controller = new ContentController()
        controller.metaClass.getPluginService = { -> [newestPlugins: { num -> [] } ] }
        controller.metaClass.getScreencastService = { -> [getLatestScreencastId: { -> 5 } ] }
        controller.metaClass.render = {Map args -> renderParams = args }

        controller.index()

        assertEquals "homePage", renderParams.view
    }


    void testIndexWikiPageNormalRequest() {
        mockRequest.method = 'POST'
        mockParams.id = 'Introduction'

        def wikiPage = new WikiPage(title: 'Introduction')
        mockDomain(WikiPage, [wikiPage])
        wikiPage.metaClass.getComments = { -> [] }

        def controller = new ContentController()

        def manager = CacheManager.create()
        manager.addCache "test1"
        Cache cache = manager.getCache("test1")
        def wikiPageService = new WikiPageService()
        wikiPageService.cacheService = new CacheService(contentCache: cache)
        controller.wikiPageService = wikiPageService

        controller.index()

        assertEquals "contentPage", renderArgs.view
        assertTrue renderArgs.model.content instanceof WikiPage
        assertEquals "Introduction", renderArgs.model.content.title

        assertEquals renderArgs.model.content, cache.get("Introduction")?.getValue()
    }

    void testIndexWikiPageAjaxRequest() {
        ContentController.metaClass.getRequest = {-> [method: "POST", xhr: true] }
        ContentController.metaClass.getParams = {-> [id: "Introduction"] }
        mockParams.id = 'Introduction'

        def wikiPage = new WikiPage(title: 'Introduction')
        mockDomain(WikiPage, [wikiPage])
        wikiPage.metaClass.getComments = { -> [] }

        def manager = CacheManager.create()
        manager.addCache "test2"
        Cache cache = manager.getCache("test2")
        def wikiPageService = new WikiPageService()
        wikiPageService.cacheService = new CacheService(contentCache: cache)
        controller.wikiPageService = wikiPageService

        controller.index()

        assertEquals "wikiShow", renderArgs.template
        assertTrue renderArgs?.model?.content instanceof WikiPage
        assertEquals "Introduction", renderArgs?.model?.content?.title

        assertEquals renderArgs?.model?.content, cache.get("Introduction")?.getValue()
    }

    void testShowWikiVersion() {
        ContentController.metaClass.getParams = {-> [id: "Introduction", number: 7] }
        WikiPage.metaClass.static.findByTitle = {String title -> new WikiPage(title: title) }
        def v
        Version.metaClass.static.findByCurrentAndNumber = {WikiPage page, Long n -> v = new Version(number: n, current: page)}

        def renderParams = [:]
        ContentController.metaClass.render = {Map args -> renderParams = args }

        def controller = new ContentController()
        controller.showWikiVersion()
        assertEquals "showVersion", renderParams.view

        assertEquals v, renderParams.model?.content
        assertEquals 7, renderParams.model?.content.number
    }

    void testShowWikiVersionNotFound() {
        ContentController.metaClass.getParams = {-> [id: "Introduction", number: 7] }
        def page
        WikiPage.metaClass.static.findByTitle = {String title -> page = new WikiPage(title: title) }
        Version.metaClass.static.findByCurrentAndNumber = {WikiPage p, Long n -> null}

        def renderParams = [:]
        ContentController.metaClass.render = {Map args -> renderParams = args }

        def controller = new ContentController()
        controller.showWikiVersion()
        assertEquals "contentPage", renderParams.view
        assertEquals page, renderParams.model.content

    }

    void testMarkupWikiPage() {
        ContentController.metaClass.getParams = {-> [id: "Introduction", number: 7] }
        def page
        WikiPage.metaClass.static.findByTitle = {String title -> page = new WikiPage(title: title) }

        def renderParams = [:]
        ContentController.metaClass.render = {Map args -> renderParams = args }

        def controller = new ContentController()
        controller.markupWikiPage()
        assertEquals "wikiFields", renderParams.template

        assertEquals page, renderParams.model?.wikiPage

    }

    void testInfoWikiPage() {
        ContentController.metaClass.getParams = {-> [id: "Introduction", number: 7] }
        def page
        WikiPage.metaClass.static.findByTitle = {String title -> page = new WikiPage(title: title) }
        Version.metaClass.static.findAllByCurrent = {Content c -> []}
        def renderParams = [:]
        ContentController.metaClass.render = {Map args -> renderParams = args }

        def controller = new ContentController()
        controller.infoWikiPage()
        assertEquals "wikiInfo", renderParams.template

        assertEquals page, renderParams.model?.wikiPage
        assertEquals([], renderParams.model.versions)

    }

    void testEditWikiPage() {
        ContentController.metaClass.getParams = {-> [id: "Introduction", number: 7] }
        def page
        WikiPage.metaClass.static.findByTitle = {String title -> page = new WikiPage(title: title) }
        Version.metaClass.static.findAllByCurrent = {Content c -> []}
        def renderParams = [:]
        ContentController.metaClass.render = {Map args -> renderParams = args }

        def controller = new ContentController()
        controller.editWikiPage()
        assertEquals "wikiEdit", renderParams.template

        assertEquals page, renderParams.model?.wikiPage

    }

    void testCreateWikiPage() {
        ContentController.metaClass.getParams = {-> [id: "Introduction"] }

        def controller = new ContentController()
        def model = controller.createWikiPage()

        assertEquals "Introduction", model.pageName

    }

    void testSaveWikiPageGET() {

        def errorArg = 404
        ContentController.metaClass.getResponse = {-> [sendError: { errorArg = it}]  }
        ContentController.metaClass.getRequest = {-> [method: "GET"] }


        def controller = new ContentController()
        def model = controller.saveWikiPage()


        assertEquals 403, errorArg
    }

    void testSaveWikiPageWhenPageNotFoundFailure() {
        ContentController.metaClass.getRequest = {-> [method: "POST"] }
        ContentController.metaClass.getParams = {-> [id: "Introduction", title: "Introduction", body: "hello"] }
        WikiPage.metaClass.static.findByTitle = {String title -> null }
        WikiPage.metaClass.setId = { }
        WikiPage.metaClass.save = {-> delegate }
        WikiPage.metaClass.hasErrors = {-> true }
        String.metaClass.encodeAsURL = {-> URLCodec.encode(delegate)}

        def renderParams = [:]
        ContentController.metaClass.render = {Map args -> renderParams = args }

        def controller = new ContentController()
        controller.saveWikiPage()

        assertEquals "createWikiPage", renderParams.view
        assertEquals "Introduction", renderParams?.model?.pageName
        assertEquals "Introduction", renderParams?.model?.wikiPage?.title

    }

    void testSaveWikiPageWhenPageNotFoundSuccess() {
        def wikiControl = mockFor(WikiPage)
        wikiControl.demand.static.findByTitle {String t -> null }
        wikiControl.demand.save {-> delegate }
        wikiControl.demand.hasErrors {-> false }
        wikiControl.demand.createVersion {->
            new Version()
        }
        def versControl = mockFor(Version)
        versControl.demand.save {-> delegate }

        mockParams.id = 'Introduction'
        mockParams.title = 'Introduction'
        mockParams.body = 'hello'
        mockRequest.method = 'POST'

        def redirectParams = [:]
        ContentController.metaClass.redirect = {Map args -> redirectParams = args }

        def controller = new ContentController()
        controller.saveWikiPage()

        assertEquals "/Introduction", redirectParams.uri
    }


    void testSaveWikiPageWhenPageExistsOptimisticLockingFailure() {
        ContentController.metaClass.getRequest = {-> [method: "POST"] }
        ContentController.metaClass.getParams = {-> [id: "Introduction", title: "Introduction", body: "hello", version: "3"] }
        WikiPage.metaClass.static.findByTitle = {String title -> new WikiPage(title: title) }
        WikiPage.metaClass.setId = { }
        WikiPage.metaClass.save = {-> delegate }
        WikiPage.metaClass.hasErrors = {-> false }
        WikiPage.metaClass.getVersion = {-> 2 }
        Version.metaClass.save = {-> delegate }

        def renderParams = [:]
        ContentController.metaClass.render = {Map args -> renderParams = args }

        def controller = new ContentController()
        controller.saveWikiPage()

        assertEquals "wikiEdit", renderParams.template
        assertEquals "page.optimistic.locking.failure", renderParams.model.error

    }
}