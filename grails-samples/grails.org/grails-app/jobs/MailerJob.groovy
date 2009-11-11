import org.grails.content.notifications.ContentAlertStack
import org.grails.meta.UserInfo
import org.grails.content.Content
import org.radeox.engine.context.BaseInitialRenderContext
import org.grails.wiki.GrailsWikiEngine
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import javax.servlet.ServletContext
import org.grails.content.Version
import org.grails.plugin.Plugin
import org.codehaus.groovy.grails.plugins.*

class MailerJob {
    def startDelay = 60000
    def timeout =    60000     // execute job every minute

    ContentAlertStack contentToMessage
    def mailService
    def pluginService
    def cacheService

    def execute() {
		try {
	        Content content = contentToMessage?.popOffStack()
	        if (content) {
	            ServletContext servletContext = ServletContextHolder.servletContext
	            def context = new BaseInitialRenderContext();
	            context.set(GrailsWikiEngine.CONTEXT_PATH, servletContext.contextPath)
	            context.set(GrailsWikiEngine.CACHE, cacheService)
	            def engine = new GrailsWikiEngine(context)

	            context.setRenderEngine engine
	            def emails = UserInfo.executeQuery("select ui.user.email from UserInfo as ui where ui.emailSubscribed = ?", [true] )
	            while (content) {
	                def plugin = pluginService.resolvePossiblePlugin(content)
	                def text = new StringBuffer()
	                def titleUrlEscaped = content.title.encodeAsURL()
	                def url = "http://grails.org/${titleUrlEscaped}"
	                def myTitle = content.title
	                // make some alterations to the email if this wiki is a part of a plugin
	                if (plugin instanceof Plugin) {
	                    url = "http://grails.org/plugin/${plugin.name}"
	                    def wikiType = content.title.split('-')[0]
	                    myTitle = "${plugin.title} (${wikiType[0].toUpperCase() + wikiType[1..-1]} tab)"
	                }

	                def pageVersions = Version.findAllByCurrent(content, [sort:'number',order:'desc', max:1, cache:true])

					if(pageVersions) {
		                def version = pageVersions[-1] //last version
						text << "<div>To unsubscribe from receiving these emails go to <a href=\"http://grails.org/profile\">http://grails.org/profile</a>, login and uncheck the 'Receive E-mail Updates for Content Changes?' box. </div><br><br>"
		                text << '<div style="color:black;"><br><br>'
		                text << "Page <a href=\"${url}\">${myTitle}<a/> <br><br>"
		                text << "</div>"
		                text << '<div style="color:black;"><br><br>'
		                text << "Edited by <b>${version.author?.login}</b>. <a href=\"http://grails.org/previous/${titleUrlEscaped}/${version.number}\">View change</a> <br><br>"
		                text << "</div>"
		                text << engine.render(content.body, context)

		                for (email in emails) {
		                    mailService?.sendMail {
		                        title "Grails > ${myTitle}"
		                        from "wiki@grails.org"
		                        replyTo "wiki@grails.org"
		                        to email
		                        html text.toString()
		                    }
		                }						
					}
	                content = contentToMessage?.popOffStack()
					Content.withSession { session -> session.clear() }
	            }
	        }		
		}
		finally {
			DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP.get().clear()
		}
    }
}
