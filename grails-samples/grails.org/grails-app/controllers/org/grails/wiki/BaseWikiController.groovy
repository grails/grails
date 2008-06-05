package org.grails.wiki

import org.radeox.engine.context.BaseInitialRenderContext
import org.radeox.engine.context.BaseRenderContext

/**
 * @author Graeme Rocher
 * @since 1.0
 * 
 * Created: May 14, 2008
 */
class BaseWikiController {

    def cacheService
    BaseRenderContext wikiContext
    GrailsWikiEngine wikiEngine
    
    GrailsWikiEngine createWikiEngine() { wikiEngine }
    BaseRenderContext getContext() { wikiContext }    

}