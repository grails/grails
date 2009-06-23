import org.codehaus.groovy.grails.web.servlet.*
class BootStrap {
	

     def init = { servletContext ->
	
			def appCtx = servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
			
			
			assert appCtx != null
     }
     def destroy = {
     }
} 