

package org.grails.plugin

import org.grails.wiki.BaseWikiController
import org.grails.wiki.WikiPage
import org.grails.comment.Comment
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.auth.User

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
        pluginMap.untagged = Plugin.withCriteria { isEmpty('tags') }
        render view:'listPlugins', model:[pluginMap: pluginMap]
    }

    def show = {
        println "show: $params"
        def plugin = byName(params)
        render view:'showPlugin', model:[plugin:plugin, comments: plugin.comments.sort { it.dateCreated }]
    }

    def editPlugin = {
        println "editPlugin: $params"
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
//        println "createPlugin:"
//        println params
//        println request
        def plugin = new Plugin(params)

        if(request.method == 'POST') {
            Plugin.WIKIS.each { wiki ->
                def body = ''
                if (wiki == 'installation') {
                    body = "{code}grails install-plugin ${plugin.name}{code}"
                }
                def wikiPage = new WikiPage(title:wiki, body:body)
                if (!wikiPage.validate()) { println wikiPage.errors }
                wikiPage.save()
                plugin."$wiki" = wikiPage
            }

            // if there is no provided doc url, we'll assume that this page is the doc
            if (!plugin.documentationUrl) {
                plugin.documentationUrl = "${ConfigurationHolder.config.grails.serverURL}/plugin/${plugin.name}"
            }

            plugin.author = request.user
            def saved = plugin.save()
            if(saved) {
                println "REDIRECTING"
                redirect(action:'show', params: [name:plugin.name])
            } else {
                return render(view:'createPlugin', model:[plugin:plugin])
            }
        } else {
            if(params.async) {
//                render(template:"newsForm", model:[plugin:plugin])
            } else {
                return render(view:'createPlugin', model:[plugin:plugin])
            }
        }
    }

    def postComment = {
        def plugin = Plugin.get(params.id)
        if (params.comment) {
            def c = new Comment(body:params.comment, user: request.user)
            plugin.addToComments(c)
            plugin.save()
        }
        redirect(action:'show', params: [name:plugin.name])
    }

    def rate = {
        println "rate:params: $params"
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

        def tag = Tag.findByName(params.newTag)
        if (!tag) {
            tag = new Tag(name: params.newTag)
            tag.save()
        }

        plugin.addToTags(tag)
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
