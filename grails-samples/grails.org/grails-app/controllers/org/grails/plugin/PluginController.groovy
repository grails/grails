

package org.grails.plugin

import org.grails.wiki.WikiPage
import org.grails.comment.Comment
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.auth.User
import org.grails.wiki.BaseWikiController
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Order

class PluginController extends BaseWikiController {

    static String HOME_WIKI = 'PluginHome'

    def wikiPageService

    def index = {
        redirect(controller:'plugin', action:home, params:params)
    }

    def home = {

        def tagCounts = Tag.list().inject([:]) { tagCount, tag ->
            tagCount[tag.name] = tag.plugins.size()
            tagCount
        }

        def popularPlugins = Plugin.withCriteria {
            createAlias("ratings", "r")
            .setProjection(Projections.projectionList()
                .add(Projections.groupProperty("name"))
                .add(Projections.groupProperty("title"))
                .add(Projections.avg("r.stars").as("avgStars"))
                .add(Projections.count("r.stars").as("numRatings"))
            ).addOrder(Order.desc("avgStars"))
            maxResults(10)
        }.inject([]) { list, result ->
            list << [[name:result[0], title:result[1]], result[2], result[3]]
        }

        def newestPlugins = Plugin.withCriteria {
            order('dateCreated', 'desc')
            maxResults(5)
        }

        def recentlyUpdatedPlugins = Plugin.withCriteria {
            order('lastReleased', 'desc')
            maxResults(5)
        }

        def latestComments = Comment.list(sort:'dateCreated', order:'desc', max:5);

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
        Tag.list().each { tag ->
            pluginMap[tag.name] = Plugin.withCriteria {
                tags {
                    eq('name', tag.name)
                }
            }.sort { it.title }
        }
        pluginMap = pluginMap.sort { it.key }
        pluginMap.untagged = Plugin.withCriteria { isEmpty('tags') }.sort { it.title }

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
        render view:'showPlugin', model:[plugin:plugin, comments: plugin.comments.sort { it.dateCreated }, userRating: userRating]
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
            searchResult.results = searchResult.results.unique { it.title }
			flash.message = "Found $searchResult.total results!"
			flash.next()
			render(view:"searchResults", model:[searchResult:searchResult])
		}
		else {
			render(view:"homePage")
		}
   }

    def postComment = {
        def plugin = Plugin.get(params.id)
        def c = new Comment(body:params.comment, user: request.user)
        plugin.addToComments(c)
        plugin.save(flush:true)
        return render(template:'/comment/comment', var:'comment', bean:c)
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
            def tag = Tag.findByName(newTag.trim())
            if (!tag) {
                tag = new Tag(name: newTag.trim())
                tag.save()
            }
            plugin.addToTags(tag)
            tag.addToPlugins(plugin)
            assert tag.save()
        }
        assert plugin.save()
        render(template:'tags', var:'plugin', bean:plugin)
    }

    def removeTag = {
        def plugin = Plugin.get(params.id)
        def tag = Tag.findByName(params.tagName)
        plugin.removeFromTags(tag)
        tag.removeFromPlugins(plugin)
        plugin.save()
        tag.save()
        render(template:'tags', var:'plugin', bean:plugin)
    }

    def showComment = {
        def plugin = Plugin.withCriteria {
            comments {
                eq('id', params.id.toLong())
            }
        }[0]
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
