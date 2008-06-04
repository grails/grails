package org.grails.wiki

import org.radeox.engine.context.BaseInitialRenderContext
import org.grails.cache.CacheService

/**
* @author Graeme Rocher
* @since 1.0
*
* Created: Feb 19, 2008
*/
class WikiTagLib  {

    static namespace = 'wiki'

    CacheService cacheService

    def text = { attrs, body ->
        def cached
        if(attrs.key) {
            cached = cacheService.getWikiText(attrs.key)
        }
        if(cached) {
            out << cached            
        }
        else {
            def context = new BaseInitialRenderContext();
            context.set(GrailsWikiEngine.CONTEXT_PATH, request.contextPath)
            context.set(GrailsWikiEngine.CACHE, cacheService)
            def engine = new GrailsWikiEngine(context)


            context.setRenderEngine engine

            def content = body()
            def text = engine.render(content.trim(), context)
            if(attrs.key) {
                cacheService.putWikiText(attrs.key, text)
            }
            out << text
        }
    }

    def editViewButton = { attrs, body ->
        def displayLinks = attrs.containsKey("displayEditLinks") ? Boolean.valueOf(attrs.displayEditLinks) : true
        out << render(template:"/content/editViewButton", model:[id:attrs.id, text:attrs.text, displayEditLinks: displayLinks, onComplete:attrs.onComplete ])
    }

}