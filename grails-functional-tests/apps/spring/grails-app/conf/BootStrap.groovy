import org.codehaus.groovy.grails.web.servlet.*
class BootStrap {
	

     def init = { servletContext ->
	
			def appCtx = servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
			
			
			assert appCtx != null
			
			new mvc.Person(name:"Fred").save(flush:true)
     }
     def destroy = {
     }
} 