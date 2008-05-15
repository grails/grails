package org.grails

import org.grails.news.NewsItem
import org.radeox.engine.context.BaseInitialRenderContext
import org.grails.wiki.GrailsWikiEngine
import org.grails.wiki.BaseWikiController

/**
 * @author Graeme Rocher
 * @since 1.0
 * 
 * Created: May 14, 2008
 */
class NewsController extends BaseWikiController{

    def cacheService

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
                redirect(uri:"")
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

    def createNews = {
        def newsItem = new NewsItem(params)
        if(request.method == 'POST') {
            newsItem.author = request.user
            if(newsItem.save()) {
                redirect(uri:"")
            }
            else {
                return [newsItem:newsItem]
            }
        }
        else {
            return [newsItem:newsItem]
        }
    }

}