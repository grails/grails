

package org.grails.plugin

import org.grails.wiki.BaseWikiController
import org.grails.wiki.WikiPage
import org.grails.comment.Comment
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class PluginController extends BaseWikiController {

    def index = {
        redirect(controller:'plugin', action:list, params:params)
    }

    def list = {
        render view:'listPlugins', model:[plugins:Plugin.list(), totalPlugins: Plugin.count()]
    }

    def show = {
        def plugin = byName(params)
        render view:'showPlugin', model:[plugin:plugin, comments: plugin.comments.sort { it.dateCreated }]
    }

    def editPlugin = {
        def plugin = byName(params)
        if(plugin) {
            if(request.method == 'POST') {
                // update plugin
                plugin.properties = params
                plugin.save(flush:true)
//                cacheService?.removeWikiText plugin.title
                redirect(action:'show', id:plugin.id)

            }
            else {
                return render(view:'editPlugin', model: [plugin:plugin])
            }
        } else {
            response.sendError 404
        }
    }

    def createPlugin = {
        println "createPlugin:"
        println params
        println request
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
