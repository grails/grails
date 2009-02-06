

package org.grails.plugin

import org.grails.wiki.BaseWikiController

class PluginController extends BaseWikiController {
    
    def scaffold = Plugin

    def showPlugin = {
        [content:Plugin.get(params.id)]
    }

    def createPlugin = {
        def plugin = new Plugin(params)
        if(request.method == 'POST') {
            plugin.author = request.user
            def saved = plugin.save()
            if(saved) {
                redirect(uri:"/")
            } else {
                return [plugin:plugin]
            }
        } else {
            if(params.async) {
//                render(template:"newsForm", model:[plugin:plugin])
            } else {
                return [plugin:plugin]
            }
        }
    }
}
