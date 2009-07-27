

package org.grails.plugin

import org.grails.wiki.WikiPage
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.wiki.BaseWikiController
import org.grails.taggable.*
import org.grails.comments.*
import net.sf.ehcache.Element

class PluginController extends BaseWikiController {

    static String HOME_WIKI = 'PluginHome'
    static int PORTAL_MAX_RESULTS = 5
    static int PORTAL_MIN_RATINGS = 1
    
    def wikiPageService
    def pluginService
    def commentService

    def index = {
        redirect(controller:'plugin', action:home, params:params)
    }

    def home = {
        params.max = 5
        params.offset = params.offset ?: 0
        params.sort = params.sort ?: 'name'
        params.order = params.order ?: 'asc'
		params.cache = true
        def category = params.remove('category') ?: 'featured'
        
        log.debug "plugin home: $params"
        
        def currentPlugins
		def totalPlugins = 0
		def defaults = {
            currentPlugins = Plugin.list(params)
			totalPlugins = Plugin.count()			
		}
        switch (category) {
            case 'all':
				defaults()
                break;
			case 'popular':
	            currentPlugins = Plugin.listOrderByAverageRating([cache:true, offset:params.offset, max:5])
				totalPlugins = Plugin.countRated()	
	            break;				
			break
			case 'newest':
				params.sort = 'dateCreated'
				params.order = 'desc'
				defaults()
				break			
			break
			case 'supported':
	            currentPlugins = Plugin.findAllByOfficial(true, params)
				totalPlugins = Plugin.countByOfficial(true)
	            break;			
			break
            case 'featured':
                currentPlugins = Plugin.findAllByFeatured(true, params)
				totalPlugins = Plugin.countByFeatured(true)
                break;
            case 'recentlyUpdated':
                params.sort = 'lastReleased'
				params.order = 'desc'
				defaults()
                break;
            default:
				defaults()
				
			break
        }
        
        [currentPlugins:currentPlugins, category:category,totalPlugins:totalPlugins]
        
    }

	def all = {
		render view:"home", model:[originAction:"all",
								  pluginList:Plugin.list(max:10, offset: params.offset?.toInteger(), cache:true, sort:"name")]
	}
	
	def pluginListCache
    def list = {
        def pluginMap = pluginListCache?.get("fullPluginList")?.value
		if(!pluginMap) {
			pluginMap = [:]
	        Tag.list(sort:'name', cache:true).each { tag ->
	            pluginMap[tag.name] = []
	            def links = TagLink.findAllByTagAndType(tag, 'plugin', [cache:true]) 
				if(links) {
					pluginMap[tag.name] = Plugin.withCriteria {
						inList 'id', links*.tagRef
						cache true
					}				
				}
	            pluginMap[tag.name].sort { it.title }
	        }
			
			pluginListCache.put new Element("fullPluginList", pluginMap)
		}

        // remove empty tags
        pluginMap = pluginMap.findAll { it.value.size() }
        def taggedIds = TagLink.withCriteria {
            eq('type', 'plugin')
            projections {
                distinct('tagRef')
            }
			cache true
        }
        pluginMap.untagged = Plugin.findAllByIdNotInList(taggedIds, [cache:true])
        render view:'listPlugins', model:[pluginMap: pluginMap]
    }

    def show = {
        def plugin = byName(params)
        if (!plugin) {
            return redirect(action:'createPlugin', params:params)
        }

        def userRating
        if (request.user) {
            userRating = plugin.userRating(request.user)
        }

        // TODO: figure out why plugin.ratings.size() is always 1
        render view:'showPlugin', model:[plugin:plugin, userRating: userRating]
    }

    def editPlugin = {
        def plugin = Plugin.get(params.id)
        if(plugin) {
            if(request.method == 'POST') {
                // update plugin
                plugin.properties = params
                if (!plugin.validate()) {
                    return render(view:'editPlugin', model: [plugin:plugin])
                }
                if (!plugin.isNewerThan(params.currentRelease)) {
                    plugin.lastReleased = new Date();
                }
                // update plugin
                plugin.properties = params
                plugin.save(flush:true)
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

            def top5 = Plugin.listOrderByLastUpdated(order:'desc', max:5, cache:true)
            title = "Grails New Plugins Feed"
            link = "http://grails.org/Plugins"
            description = "New and recently updated Grails Plugins"

            for(item in top5) {
                entry(item.title) {
                    link = "http://grails.org/plugin/${item.name.encodeAsURL()}"
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
        def link = CommentLink.findByCommentAndType(Comment.get(params.id), 'plugin', [cache:true])
        def plugin = Plugin.get(link.commentRef)
        redirect(action:'show', params:[name:plugin.name], fragment:"comment_${params.id}")
    }

    private def pluginWiki(name, plugin, params) {
        plugin."$name" = new WikiPage(title:name, body:params."$name")
    }

    private def byTitle(params) {
        Plugin.findByTitle(params.title.replaceAll('\\+', ' '), [cache:true])
    }

    private def byName(params) {
        Plugin.findByName(params.name, [cache:true])
    }
}