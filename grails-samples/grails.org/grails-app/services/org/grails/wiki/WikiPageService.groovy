package org.grails.wiki
/*
 * author: Matthew Taylor
 */
class WikiPageService {

    def cacheService
    
    def getCachedOrReal(id) {
         id = id.decodeURL()

         def wikiPage = cacheService.getContent(id)
            if(!wikiPage) {
                wikiPage = WikiPage.findByTitle(id, [cache:true])
                if(wikiPage) cacheService.putContent(id, wikiPage)
            }
         return wikiPage
    }

}