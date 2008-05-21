package org.grails.wiki

import org.grails.ContentController
import org.grails.news.NewsItem
import org.grails.blog.BlogEntry
import org.grails.content.Content
import org.grails.content.Version
import org.grails.NewsController

/**
 * @author Graeme Rocher
 * @since 1.0
 * 
 * Created: May 14, 2008
 */
class NewsControllerTests extends GroovyTestCase{

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
        remove NewsController
    }



    void testShowNews() {
        NewsItem item
        NewsItem.metaClass.static.get = { id -> item  = new NewsItem() }

        NewsController.metaClass.getParams = {-> [id:10] }


        def controller = new NewsController()
        def model = controller.showNews()

        assert item

        assertEquals item, model.content
    }

    void testCreateNewsItemGET() {
        NewsController.metaClass.getRequest ={-> [method:"GET"] }
        NewsController.metaClass.getParams ={-> [title:"New Item", body:"Some news"] }


        def controller = new NewsController()
        def model = controller.createNews()

        assert model.newsItem
        assertEquals "New Item", model.newsItem.title
        assertEquals "Some news", model.newsItem.body
    }

    void testCreateNewsItemValidationError() {
        NewsController.metaClass.getRequest ={-> [method:"POST"] }
        NewsController.metaClass.getParams ={-> [title:"New Item", body:"Some news"] }

        NewsItem.metaClass.save = {-> null }


        def controller = new NewsController()
        def model = controller.createNews()

        assert model.newsItem
        assertEquals "New Item", model.newsItem.title
        assertEquals "Some news", model.newsItem.body
    }

    void testCreateNewsItemValidationSuccess() {
        NewsController.metaClass.getRequest ={-> [method:"POST"] }
        NewsController.metaClass.getParams ={-> [title:"New Item", body:"Some news"] }
        def redirectParams = [:]

        NewsController.metaClass.redirect = { Map args -> redirectParams = args }

        NewsItem.metaClass.save = {-> delegate }


        def controller = new NewsController()
        controller.createNews()

        assertEquals "/", redirectParams.uri
    }    
}