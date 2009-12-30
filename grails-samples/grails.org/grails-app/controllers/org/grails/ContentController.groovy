package org.grails


import javax.servlet.ServletContext
import org.springframework.web.multipart.MultipartFile
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.wiki.WikiPage
import org.grails.content.Version
import org.grails.content.notifications.ContentAlertStack
import org.grails.wiki.BaseWikiController
import org.grails.plugin.Plugin
import org.grails.content.Content
import org.grails.plugin.PluginController
import org.grails.screencasts.Screencast
import org.grails.blog.BlogEntry

class ContentController extends BaseWikiController {

    def screencastService
    def pluginService
    def dateService
    def textCache
    
    static accessControl = {
        // Alternatively, several actions can be specified.
        role(name: 'Editor', only:['createNews','editWikiPage','markupWikiPage', 'saveWikiPage', 'createWikiPage', 'postComment'] )
        role(name: 'Administrator', action: 'rollbackWikiVersion' )
    }

    def cacheService
    def wikiPageService

    ContentAlertStack contentToMessage

   def search = {
		if(params.q) {
			def searchResult = WikiPage.search(params.q, offset: params.offset, escape:true)
            def filtered = searchResult.results.unique { it.title }.collect {
                pluginService.resolvePossiblePlugin(it)
            }.findAll {it} // gets rid of nulls
			searchResult.results = filtered
			searchResult.total = filtered.size()
			flash.message = "Found $searchResult.total results!"
			flash.next()
			render(view:"/searchable/index", model:[searchResult:searchResult])
		}
		else {
			render(view:"homePage")
		}
   }

   def latest = {

         def engine = createWikiEngine()

         def feedOutput = {

            def top5 = WikiPage.listOrderByLastUpdated(order:'desc', max:5)
            title = "Grails.org Wiki Updates"
            link = "http://grails.org/wiki/latest?format=${request.format}"
            description = "Latest wiki updates Grails framework community"

            for(item in top5) {
                entry(item.title) {
                    link = "http://grails.org/${item.title.encodeAsURL()}"
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

    def previewWikiPage = {
        def page = WikiPage.findByTitle(params.id?.decodeURL())
        if(page) {
            def engine = createWikiEngine()
            page.discard()
            page.properties = params

            render( engine.render(page.body, context) )
        }
    }

    def index = {
        def pageName = params.id

        if(pageName) {
            if(pageName == 'Home') {
                renderHomepage()
            }
            // treat plugin pages differently
            else if (pageName.matches(/(${Plugin.WIKIS.join('|')})-[0-9]*/)) {
                // name will look like this: description-34
                // last half is plugin id
                def plugin = Plugin.get(pageName.split('-')[1])
                if (request.xhr) {
                    // update the wikiTab, not the whole contentPane
                    def wikiPage = wikiPageService.getCachedOrReal(pageName)
                    return render(template:"wikiShow", model:[content:wikiPage, update:"${pageName.split('-')[0]}Tab"])
                }
                redirect(controller:'plugin', action:'show', params:[name:plugin.name])
            }
            else {
                def wikiPage = wikiPageService.getCachedOrReal(pageName)
                if(wikiPage) {
                    if(request.xhr) {
                        render(template:"wikiShow", model:[content:wikiPage, update:params.update])
                    } else {
						// disable comments
						render(view:"contentPage", model:[content:wikiPage])
                    }
                }
                else {
                    response.sendError 404
                }
            }
		} else {
			renderHomepage()
		}
	}

    def postComment = {
        def content = Content.get(params.id)
        content.addComment(request.user, params.comment)
        render(template:'/comments/comment', var:'comment', bean:content.comments[-1])
    }

    def showWikiVersion = {
        def page = WikiPage.findByTitle(params.id.decodeURL())
        def version
        if(page) {
            version = Version.findByCurrentAndNumber(page, params.number.toLong())
        }

        if(version) {
            render(view:"showVersion", model:[content:version, update:params.update])                    
        }
        else {
            render(view:"contentPage", model:[content:page])
        }

    }

    def markupWikiPage = {
        def page = WikiPage.findByTitle(params.id.decodeURL())

        if(page) {
            render(template:"wikiFields", model:[wikiPage:page])
        }
    }

	def infoWikiPage = {
        def page = WikiPage.findByTitle(params.id.decodeURL(), [cache:true])

        if(page) {

            def pageVersions = Version.withCriteria {
				projections {
					distinct 'number', 'version'
					property 'author'
				}
				eq 'current', page
				order 'number', 'asc'
				cache true
			}
			def first = pageVersions ? Version.findByNumberAndCurrent(pageVersions[0][0], page, [cache:true]) : null
			def last  = pageVersions ? Version.findByNumberAndCurrent(pageVersions[-1][0], page, [cache:true]) : null

            render(template:'wikiInfo',model:[first:first, last:last,wikiPage:page, 
											 versions:pageVersions.collect { it[0]}, 
 											 authors:pageVersions.collect { it[1]}, 
											 update:params.update])
        }

    }

	def editWikiPage = {
        if(!params.id) {
            render(template:"/shared/remoteError", [code:"page.id.missing"])
        }
        else {
            def page = WikiPage.findByTitle(params.id.decodeURL())

            render(template:"wikiEdit",model:[wikiPage:page, update: params.update, editFormName: params.editFormName])
        }
    }

    def createWikiPage = {
        if (params.xhr) {
            return render(template:'wikiCreate', var:'pageName', bean:params.id?.decodeURL())
        }
        [pageName:params.id?.decodeURL()]
    }

    def saveWikiPage = {
      if(request.method == 'POST') {
          if(!params.id) {
                render(template:"/shared/remoteError", model:[code:"page.id.missing"])
            }
            else {
                WikiPage page = WikiPage.findByTitle(params.id.decodeURL())
                if(!page) {
                    page = new WikiPage(params)
                    if (page.locked == null) page.locked = false
                    page.save()
                    if(page.hasErrors()) {
                        render(view:"createWikiPage", model:[pageName:params.id, wikiPage:page])
                    }
                    else {
                        Thread.start {
                            contentToMessage?.pushOnStack page
                        }
                        Version v = page.createVersion()
                        v.author = request.user
                        assert v.save()

                        redirect(uri:"/${page.title.encodeAsURL()}")
                    }
                }
                else {
                    if(page.version != params.version.toLong()) {
                        render(template:"wikiEdit",model:[wikiPage:page, error:"page.optimistic.locking.failure"])
                    }
                    else {

                        page.body = params.body
                        page.lock()
                        page.version = page.version+1
                        page.save(flush:true)
                        // refresh the textCache
                        textCache.flush()

                        if(page.hasErrors()) {
                            render(template:"wikiEdit",model:[wikiPage:page])
                        }
                        else {
                            Thread.start {
                                contentToMessage?.pushOnStack page
                            }

                            Version v = page.createVersion()
                            v.author = request.user                            
							assert v.save()

                            evictFromCache(params.id)
                            render(template:"wikiShow", model:[content:page, message:"wiki.page.updated", update: params.update])
                        }
                    }
                }
            }
          
      }
      else {
          response.sendError(403)
      }
    }

    private evictFromCache(id) {
        id = id.decodeURL()
        cacheService.removeWikiText(id)
        cacheService.removeContent(id)

    }

    def rollbackWikiVersion = {
        if(request.method == 'POST') {
            def page = WikiPage.findByTitle(params.id.decodeURL())
            if(page) {
                def version = Version.findByCurrentAndNumber(page, params.number.toLong())
                if(!version) {
                   render(template:"versionList", model:[wikiPage: page,versions:Version.findAllByCurrent(page), message:"wiki.version.not.found"])
                }
                else {
                    if(page.body == version.body) {
                        render(template:"versionList", model:[wikiPage: page,versions:Version.findAllByCurrent(page), message:"Contents are identical, no need for rollback."])     
                    }
                    else {

                        page.lock()
                        page.version = page.version+1
                        page.body = version.body
                        assert page.save(flush:true)
                        Version v = page.createVersion()
                        v.author = request.user                        
                        assert v.save()
                        evictFromCache params.id

                        render(template:"versionList", model:[wikiPage: page, versions:Version.findAllByCurrent(page).sort { it.number }, message:"Page rolled back, a new version ${v.number} was created"])
                    }
                }
            }
            else {
                render(template:"versionList", model:[wikiPage: page,versions:Version.findAllByCurrent(page), message:"wiki.page.not.found"])
            }
        }
        else {
            response.sendError(403)
        }
    }

    def diffWikiVersion = {

        def page = WikiPage.findByTitle(params.id.decodeURL())
        if(page) {
            def leftVersion = params.number.toLong()
            def left = Version.findByCurrentAndNumber(page, leftVersion)
            def rightVersion = params.diff.toLong()
            def right = Version.findByCurrentAndNumber(page, rightVersion)
            if(left && right) {
                return [message: "Showing difference between version ${leftVersion} and ${rightVersion}", text1:right.body.encodeAsHTML(), text2: left.body.encodeAsHTML()]
            }
            else {
                return [message: "Version not found in diff"]
            }

        }
        else {
            return [message: "Page not found to diff" ]
        }
    }

    def previousWikiVersion = {
        def page = WikiPage.findByTitle(params.id.decodeURL())
        if(page) {
            def leftVersion = params.number.toLong()
            def left = Version.findByCurrentAndNumber(page, leftVersion)

            List allVersions = Version.findAllByCurrent(page).sort { it.number }
            def right = allVersions[allVersions.indexOf(left)-1]
            def rightVersion = right.number

            if(left && right) {
                render(view:"diffView",model:[content:page,message: "Showing difference between version ${leftVersion} and ${rightVersion}", text1:right.body.encodeAsHTML(), text2: left.body.encodeAsHTML()])
            }
            else {
                render(view:"diffView",model:[message: "Version not found in diff"])
            }

        }
        else {
            render(view:"diffView",model: [message: "Page not found to diff" ] )
        }

    }

    def uploadImage = {
        def config = ConfigurationHolder.getConfig()
        if(request.method == 'POST') {
            MultipartFile file = request.getFile('file')
            ServletContext context = getServletContext()
            def path = context.getRealPath("/images${ params.id ? '/' + params.id.encodeAsURL() : '' }" )
            log.info "Uploading image, file: ${file.originalFilename} (${file.contentType}) to be saved at $path"
            if(config.wiki.supported.upload.types?.contains(file.contentType)) {
                def newFilename = file.originalFilename.replaceAll(/\s+/, '_')
                File targetFile = new File("$path/${newFilename}")
                if(!targetFile.parentFile.exists()) targetFile.parentFile.mkdirs()
                log.info "Target file: ${targetFile.absolutePath}"
                try {
                    log.info "Attempting file transfer..."
                    file.transferTo(targetFile)
                    log.info "Success! Rendering message back to view"
                    render(view:"/common/iframeMessage", model:[pageId:"upload",
                            frameSrc: g.createLink(controller:'content', action:'uploadImage', id:params.id),
                            message: "Upload complete. Use the syntax !${params.id ? params.id.encodeAsURL() + '/' : ''}${newFilename}! to refer to your file"])
                } catch (Exception e) {
                    log.error(e.message, e)
                    render(view:"/common/uploadDialog",model:[category:params.id,message:"Error uploading file!"])
                }
            }
            else {
                log.info "Bad file type, rendering error message to view"
                render(view:"/common/uploadDialog",model:[category:params.id,message:"File type not in list of supported types: ${config.wiki.supported.upload.types?.join(',')}"])
            }
        }
        else {
            render(view:"/common/uploadDialog", model:[category:params.id])
        }
    }
    
    def renderHomepage = {
        // Homepage needs latest plugins
        def newestPlugins = pluginService.newestPlugins(4)
        def newsItems = BlogEntry.list(max:3, cache:true, order:"desc", sort:"dateCreated")
        // make it easy to get the month and day
        newsItems.each {
            it.metaClass.getMonth = { ->
                dateService.getMonthString(it.dateCreated)
            }
            it.metaClass.getDay = { ->
                dateService.getDayOfMonth(it.dateCreated)
            }
        }
        def latestScreencastId = screencastService.latestScreencastId
        render(view:"homePage", 
                model:[
                    newestPlugins:newestPlugins, 
                    newsItems:newsItems,
                    latestScreencastId: latestScreencastId,
					newsComments:newsItems?.collect { it.comments },
					newsTags:newsItems?.collect { it.tags }
                ]
        )
    }
}
