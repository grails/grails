package org.grails.wiki

import org.radeox.engine.context.BaseInitialRenderContext

/**
 * @author Graeme Rocher
 * @since 1.0
 * 
 * Created: May 14, 2008
 */
class BaseWikiController {

    def cacheService
    def context = new BaseInitialRenderContext();
    
    GrailsWikiEngine createWikiEngine() {

        context.set(GrailsWikiEngine.CONTEXT_PATH, request.contextPath)
        context.set(GrailsWikiEngine.CACHE, cacheService)
        def engine = new GrailsWikiEngine(context)
        context.setRenderEngine engine
        return engine
    }

}