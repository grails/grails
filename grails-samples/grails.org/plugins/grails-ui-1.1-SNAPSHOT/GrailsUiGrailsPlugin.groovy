import org.springframework.beans.factory.config.MethodInvokingFactoryBean

class GrailsUiGrailsPlugin {
    def version = "1.1-SNAPSHOT"
    def dependsOn = [
            bubbling:"1.5.0 > *",
            yui:"2.6.0 > *"
    ]

    def author = "Matthew Taylor"
    def authorEmail = "rhyolight@gmail.com"
    def title = "Grails UI"
    def description = '''
Provides a standard UI tag library for ajaxy widgets using YUI.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/GrailsUI+Plugin"

    def doWithSpring = {
        grailsUIConfig(MethodInvokingFactoryBean) {
            targetObject = new ConfigSlurper()
            targetMethod = 'parse'
            arguments = [application.classLoader.loadClass('GrailsUIConfig')]
        }
    }
   
    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)		
    }

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional)
    }
	                                      
    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }
	
    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
