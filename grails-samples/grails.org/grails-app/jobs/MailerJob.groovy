import org.grails.content.notifications.ContentAlertStack
import org.grails.meta.UserInfo
import org.grails.content.Content
import org.radeox.engine.context.BaseInitialRenderContext
import org.grails.wiki.GrailsWikiEngine
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import javax.servlet.ServletContext
import org.grails.content.Version

class MailerJob {
    def startDelay = 6000
    def timeout = 60000     // execute job every minute

    ContentAlertStack contentToMessage
    def mailService
    def cacheService
    
    def execute() {
        println "MAILING!!"
        Content content = contentToMessage?.popOffStack()
        if(content) {

            ServletContext servletContext = ServletContextHolder.servletContext
            def context = new BaseInitialRenderContext();
            context.set(GrailsWikiEngine.CONTEXT_PATH, servletContext.contextPath)
            context.set(GrailsWikiEngine.CACHE, cacheService)
            def engine = new GrailsWikiEngine(context)


            context.setRenderEngine engine
            def users = UserInfo.findAllByEmailSubscribed(true).user
            while(content) {
 	           	for(user in users) {
                     def text = new StringBuffer()
                     def titleUrlEscaped = content.title.encodeAsURL()

                     def pageVersions = Version.findAllByCurrent(content)
                     pageVersions = pageVersions.sort { it.number }


                     def version = pageVersions[-1] //last version
                     text << '<div style="color:black;"><br><br>'
                     text << "Page <a href=\"http://grails.org/${titleUrlEscaped}\">${content.title}<a/> <br><br>"
                     text << "</div>"
                     text << '<div style="color:black;"><br><br>'
                     text << "Edited by <b>${version.author?.login}</b>. <a href=\"http://grails.org/previous/${titleUrlEscaped}/${version.number}\">View change</a> <br><br>"
                     text << "</div>"
                     text << engine.render(content.body, context)

                     mailService?.sendMail {
                         title "Grails > ${content.title}"
                         from "wiki@grails.org"
                         replyTo "wiki@grails.org"
                         to user.email
                         html text.toString()
                     }
	            }
                content = contentToMessage?.popOffStack()	
            }
        }
        // execute task
	}
}
