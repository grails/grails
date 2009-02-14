

package org.grails.plugin

import org.grails.wiki.BaseWikiController
import org.grails.wiki.WikiPage

class PluginController extends BaseWikiController {

    def index = {
        redirect(controller:'plugin', action:list, params:params)
    }

    def list = {
        render view:'listPlugins', model:[plugins:Plugin.list(), totalPlugins: Plugin.count()]
    }

    def show = {
        def plugin = byName(params)
        render view:'showPlugin', model:[plugin:plugin, comments: plugin.comments]
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
        def wikiList = Plugin.WIKIS.inject([:]) { wikiMap, wiki ->
            if (params."$wiki") {
                wikiMap."$wiki" = params.remove(wiki)
            }
            wikiMap
        }
        // create a new plugin with everything except the wiki data
        def plugin = new Plugin(params)
        // go through and create wikis seperately
        Plugin.WIKIS.each { wiki ->
            pluginWiki wiki, plugin, wikiList
        }

        if(request.method == 'POST') {
            plugin.author = request.user
            def saved = plugin.save()
            if(saved) {
                redirect(uri:"/")
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
