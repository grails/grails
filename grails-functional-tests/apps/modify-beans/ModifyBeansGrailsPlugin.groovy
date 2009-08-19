class ModifyBeansGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    def loadAfter = [ "controllers", "converters" ]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Peter Ledbrook"
    def authorEmail = ""
    def title = "Modify Beans (for Grails functional tests)"
    def description = '''\\
This plugin exists purely to test that plugins can modify existing bean
definitions.
'''

    // URL to the plugin's documentation
    def documentation = ""

    def doWithSpring = {
        // Add a couple of extra resource bundles to the Spring message
        // source. This checks that you can call a method on a property
        // of an existing bean definition.
        def otherFiles = [
            "WEB-INF/org/example/MainConstants",
            "WEB-INF/org/example/MainMessages"
        ]
        messageSource.basenames = (messageSource.basenames.toList() + otherFiles).toArray()

        // Change the locale URL parameter to "language" from the original
        // "lang". This checks that the + operator works OK and that it's
        // possible to access properties of a bean definition property
        // value. It's a silly example, but works.
        localeChangeInterceptor.paramName += "uage${errorsXmlMarshallerRegisterer.converterClass.name.size()}"
    }

    def doWithApplicationContext = { applicationContext ->
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
