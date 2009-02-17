package org.grails

import org.grails.news.NewsItem
import org.radeox.engine.context.BaseInitialRenderContext
import org.grails.wiki.GrailsWikiEngine
import org.grails.wiki.BaseWikiController
import org.grails.cache.CacheService

/**
 * @author Graeme Rocher
 * @since 1.0
 * 
 * Created: May 14, 2008
 */
class NewsController extends BaseWikiController{

    def scaffold = NewsItem

    CacheService cacheService

    def latest = {

         def engine = createWikiEngine() 

         def feedOutput = {
                    def top5 = NewsItem.listOrderByDateCreated(order:'desc', max:5)
                    title = "Grails.org News Feed"
                    link = "http://grails.org/news/latest.${request.format}"
                    description = "Latest news and announcements from the Grails framework community"

                    for(item in top5) {
                        entry(item.title) {
                            link = "http://grails.org/news/${item.id}"
                            publishedDate = item.dateCreated
                            engine.render(item.body, context)
                        }
                    }

         }

        withFormat {
            html {
                redirect(uri:"/")
            }
            rss {
                render(feedType:"rss",feedOutput) 
            }
            atom {
                render(feedType:"atom", feedOutput)
            }
        }
    }

    def showNews = {
        [content:NewsItem.get(params.id)]
    }

    def editNews = {
        def newsItem = NewsItem.get(params.id)
        if(newsItem) {

            if(request.method == 'POST') {
                // update news
                newsItem.properties = params
                newsItem.save(flush:true)
                cacheService?.removeWikiText newsItem.title
                redirect(action:'showNews', id:newsItem.id)

            }
            else {
                return  [content:newsItem]
            }
        }
        else {
            response.sendError 404
        }
    }

    def createNews = {
        def newsItem = new NewsItem(params)
        if(request.method == 'POST') {
            newsItem.author = request.user
            if(newsItem.save()) {
                redirect(uri:"/")
            }
            else {
                return [newsItem:newsItem]
            }
        }
        else {
            if(params.async) {
                render(template:"newsForm", model:[newsItem:newsItem])
            }
            else {
                return [newsItem:newsItem]
            }
        }
    }

}