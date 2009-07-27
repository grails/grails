package org.grails.wiki

import org.radeox.engine.context.BaseInitialRenderContext
import org.grails.cache.CacheService
import org.radeox.engine.context.BaseRenderContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationContext

/**
* @author Graeme Rocher
* @since 1.0
*
* Created: Feb 19, 2008
*/
class WikiTagLib implements ApplicationContextAware  {

    static namespace = 'wiki'

    CacheService cacheService
	def wikiPageService
    ApplicationContext applicationContext

	
	def shorten = { attrs, body ->
		def text = attrs.text
		def length = attrs.length?.toInteger() ?: 25
		
		if(text.length() > length) {
			out << "${text[0..length]}..."
		}
		else {
			out << text
		}
	}
	
    def preview = { attrs, body ->
        def engine = applicationContext.getBean('wikiEngine')
        def context = applicationContext.getBean('wikiContext')

        def content = body()
        def text = engine.render(content.trim(), context)


        if(text.size() > 150) {
            text = text.replaceAll(/<.+?>/, '').replaceAll(/<\/\S+?>/, '')
            out << text
        }
        else {
            out << text
        }
    }

    def text = { attrs, body ->
        def cached
        if(attrs.key) {
            cached = cacheService.getWikiText(attrs.key)
        }
        if(cached) {
            out << cached            
        }
        else {
            def engine = applicationContext.getBean('wikiEngine')
            def context = applicationContext.getBean('wikiContext')

            def content 
			if(attrs.page) {
 				content = wikiPageService?.getCachedOrReal(attrs.page)?.body ?: ""
			}
			else {
				content  = body()
			}
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