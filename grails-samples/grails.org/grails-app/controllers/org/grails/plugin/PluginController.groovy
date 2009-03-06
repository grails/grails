

package org.grails.plugin

import org.grails.wiki.WikiPage
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.auth.User
import org.grails.wiki.BaseWikiController
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Order
import org.grails.taggable.Tag
import org.grails.taggable.TagLink
import org.grails.comments.Comment
import org.grails.comments.CommentLink

class PluginController extends BaseWikiController {

    static String HOME_WIKI = 'PluginHome'

    def wikiPageService

    def index = {
        redirect(controller:'plugin', action:home, params:params)
    }

    def home = {

        def tagCounts = [:]
        TagLink.withCriteria {
            eq('tagClass', Plugin.class.name)
            projections {
                groupProperty('tag')
                count('tagRef')
            }
        }.each {
            // TODO: put multiple assignment back in place as soon as IntelliJ catches up, because right now it thinks
            // this entire source file is invalid when I do this:
            //      def (tagName, count) = it
            def tagName = it[0]
            def count = it[1]
            tagCounts[tagName] = tagCounts[tagName] ? (tagCounts[tagName] + count) : count
        }

        // TODO: put a criteria restriction in place that will only return plugins that have at least 3 votes.
        // I can't figure out how restrict a projection within a criteria, so I'm just doing a 'manual' filter
        // after I've gotten the results back.
        def popularPlugins = Plugin.withCriteria {
            createAlias("ratings", "r")
            .setProjection(Projections.projectionList()
                .add(Projections.groupProperty("name"))
                .add(Projections.groupProperty("title"))
                .add(Projections.avg("r.stars").as("avgStars"))
                .add(Projections.count("r.stars").as("numRatings"))
            ).addOrder(Order.desc("avgStars"))
            maxResults(5)
        }.inject([]) { list, result ->
            // this is my 'manual' filter to only get plugins with at least 5 votes
            if (result[3] >= 3) {
                list << [[name:result[0], title:result[1]], result[2], result[3]]
            }
            list
        }

        def newestPlugins = Plugin.withCriteria {
            order('dateCreated', 'desc')
            maxResults(5)
        }

        def recentlyUpdatedPlugins = Plugin.withCriteria {
            order('lastReleased', 'desc')
            maxResults(5)
        }

        def latestComments = CommentLink.withCriteria {
            eq 'type', 'plugin'
            comment {
                order('dateCreated', 'desc')
            }
            maxResults 5
        }*.comment
    
        def homeWiki = wikiPageService.getCachedOrReal(HOME_WIKI)
        if (!homeWiki) {
            homeWiki = new WikiPage(title:HOME_WIKI, body: 'Please edit me.').save()
        }
        [
                homeWiki: homeWiki,
                tagCounts: tagCounts,
                popularPlugins: popularPlugins,
                newestPlugins: newestPlugins,
                recentlyUpdatedPlugins: recentlyUpdatedPlugins,
                latestComments: latestComments
        ]
    }

    def list = {
        def pluginMap = [:]
        Tag.list(sort:'name').each { tag ->
            pluginMap[tag.name] = []
            def links = TagLink.withCriteria {
                eq('tag', tag)
                eq('tagClass', Plugin.class.name)
            }
            links.each { link ->
                def p = Plugin.get(link.tagRef)
                if (p) pluginMap[tag.name] << p 
            }
            pluginMap[tag.name].sort { it.title }
        }
        // remove empty tags
        pluginMap = pluginMap.findAll { it.value.size() }
        def taggedIds = TagLink.withCriteria {
            eq('tagClass', Plugin.class.name)
            projections {
                distinct('tagRef')
            }
        }
        pluginMap.untagged = Plugin.findAllByIdNotInList(taggedIds)
        render view:'listPlugins', model:[pluginMap: pluginMap]
    }

    def show = {
        def plugin = byName(params)
        if (!plugin) {
            return redirect(action:'createPlugin', params:params)
        }
        def userRating
        if (request.user) {
            userRating = Plugin.withCriteria {
                eq('id', plugin.id)
                ratings {
                    eq('user', request.user)
                }
            }
        }
        // TODO: figure out why plugin.ratings.size() is always 1
        render view:'showPlugin', model:[plugin:plugin, userRating: userRating]
    }

    def editPlugin = {
        def plugin = Plugin.get(params.id)
        if(plugin) {
            if(request.method == 'POST') {
                if (params.currentRelease && plugin.currentRelease != params.currentRelease) {
                    plugin.lastReleased = new Date();
                }
                // update plugin
                plugin.properties = params
                plugin.save(flush:true)
//                cacheService?.removeWikiText plugin.title
                redirect(action:'show', params:[name:plugin.name])
            } else {
                return render(view:'editPlugin', model: [plugin:plugin])
            }
        } else {
            response.sendError 404
        }
    }

    def createPlugin = {
        // just in case this was an ad hoc creation where the user logged in during the creation...
        if (params.name) params.name = params.name - '?action=login'
        def plugin = new Plugin(params)
        if(request.method == 'POST') {
            plugin.save(flush:true)
            Plugin.WIKIS.each { wiki ->
                def body = ''
                if (wiki == 'installation') {
                    body = "{code}grails install-plugin ${plugin.name}{code}"
                }
                def wikiPage = new WikiPage(title:"${wiki}-${plugin.id}", body:body)
                wikiPage.save()
                plugin."$wiki" = wikiPage
            }

            // if there is no provided doc url, we'll assume that this page is the doc
            if (!plugin.documentationUrl) {
                plugin.documentationUrl = "${ConfigurationHolder.config.grails.serverURL}/plugin/${plugin.name}"
            }

            plugin.author = request.user
            plugin.lastReleased = new Date()
            if(plugin.save()) {
                redirect(action:'show', params: [name:plugin.name])
            } else {
                return render(view:'createPlugin', model:[plugin:plugin])
            }
        } else {
            return render(view:'createPlugin', model:[plugin:plugin])
        }
    }

    def deletePlugin = {
        def plugin = byName(params)
        log.warn "Deleting Plugin: $plugin"
        plugin.delete()
        redirect(view:'index')
    }

    def search = {
		if(params.q) {
            def searchResult = Plugin.search(params.q, reload: true, offset: params.offset, escape:true)
            searchResult.results = searchResult.results.findAll{it}.unique { it.title }
			flash.message = "Found $searchResult.total results!"
			flash.next()
			render(view:"searchResults", model:[searchResult:searchResult])
		}
		else {
			redirect(action:'home')
		}
   }

    def latest = {

        def engine = createWikiEngine()

         def feedOutput = {

            def top5 = Plugin.listOrderByLastUpdated(order:'desc', max:5)
            title = "Grails New Plugins Feed"
            link = "http://grails.org/plugin/latest?format=${request.format}"
            description = "New and recently updated Grails Plugins"

            for(item in top5) {
                entry(item.title) {
                    link = "http://grails.org/plugin/${item.name.encodeAsURL()}"
                    categories = item.tags*.name
                    author = item.author
                    publishedDate = item.lastUpdated
                    engine.render(item.description.body, context)
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

    def postComment = {
        def plugin = Plugin.get(params.id)
        plugin.addComment(request.user, params.comment)
        plugin.save(flush:true)
        return render(template:'/comments/comment', var:'comment', bean:plugin.comments[-1])
    }

    def rate = {
        def plugin = Plugin.get(params.id)
        def user = request.user
        def users = plugin.ratings*.user
        def rating = params.rating.toInteger()
        // for new ratings, create a new one
        if (!users || !(user in users) ) {
            plugin.addToRatings(stars:rating, user: User.get(user.id))
            plugin.save()
        }
        // update the current rating if the user has already rated it
        else {
            def oldRating = plugin.ratings.find { it.user == user }
            oldRating.stars = rating
            assert oldRating.save()
        }
        render "${plugin.avgRating},${plugin.ratings.size()}"
    }

    def addTag = {
        def plugin = Plugin.get(params.id)
        params.newTag.trim().split(',').each { newTag ->
            plugin.addTag(newTag.trim())
        }
        assert plugin.save()
        render(template:'tags', var:'plugin', bean:plugin)
    }

    def removeTag = {
        def plugin = Plugin.get(params.id)
        plugin.removeTag(params.tagName)
        plugin.save()
        render(template:'tags', var:'plugin', bean:plugin)
    }

    def showTag = {
        redirect(action:'list', fragment:"${params.selectedTag} tags")
    }

    def showComment = {
        def link = CommentLink.findByCommentAndType(Comment.get(params.id), 'plugin')
        def plugin = Plugin.get(link.commentRef)
        redirect(action:'show', params:[name:plugin.name], fragment:"comment_${params.id}")
    }

    private def pluginWiki(name, plugin, params) {
        plugin."$name" = new WikiPage(title:name, body:params."$name")
    }

    private def byTitle(params) {
        Plugin.findByTitle(params.title.replaceAll('\\+', ' '))
    }

    private def byName(params) {
        Plugin.findByName(params.name)
    }
}
