

package org.grails.plugin

import org.grails.wiki.WikiPage
import org.grails.comment.Comment
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.auth.User
import org.grails.wiki.BaseWikiController

class PluginController extends BaseWikiController {

    def index = {
        redirect(controller:'plugin', action:list, params:params)
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
            Plugin.WIKIS.each { wiki ->
                def body = ''
                if (wiki == 'installation') {
                    body = "{code}grails install-plugin ${plugin.name}{code}"
                }
                def wikiPage = new WikiPage(title:wiki, body:body)
                wikiPage.save()
                plugin."$wiki" = wikiPage
            }

            // if there is no provided doc url, we'll assume that this page is the doc
            if (!plugin.documentationUrl) {
                plugin.documentationUrl = "${ConfigurationHolder.config.grails.serverURL}/plugin/${plugin.name}"
            }

            plugin.author = request.user
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
        redirect(view:'list')
    }

    def postComment = {
        def plugin = Plugin.get(params.id)
        def frag = 'comments'
        if (params.comment) {
            def c = new Comment(body:params.comment, user: request.user)
            plugin.addToComments(c)
            plugin.save(flush:true)
            frag = "comment_${c.id}"
        }
        redirect(action:'show', params: [name:plugin.name], fragment:frag)
    }

    def rate = {
        def plugin = Plugin.get(params.id)
        def user = request.user
        // only save if the ip has not already rated
        def users = plugin.ratings*.user
        if (!users || !(user in users) ) {
            def rating = params.rating.toInteger()
            plugin.addToRatings(stars:rating, user: User.get(user.id))
            plugin.save()
        }
        render "${plugin.avgRating},${plugin.ratings.size()}"
    }

    def addTag = {
        def plugin = Plugin.get(params.id)
        params.newTag.trim().split(',').each { newTag ->
            def tag = Tag.findByName(newTag)
            if (!tag) {
                tag = new Tag(name: newTag)
                tag.save()
            }
            plugin.addToTags(tag)
        }
        assert plugin.save()
        render(template:'tags', var:'plugin', bean:plugin)
    }

    def removeTag = {
        def plugin = Plugin.get(params.id)
        plugin.tags.remove(Tag.findByName(params.tagName))
        plugin.save()
        render(template:'tags', var:'plugin', bean:plugin)
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
