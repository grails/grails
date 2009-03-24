class DbUtilGrailsPlugin {
    def version = 0.3
    def dependsOn = [:]

    def author = "Tyler Williams"
    def authorEmail = "kaerfredoc@gmail.com"
    def title = "DB Utils - Embedded"
    def description = '''Embedded dbs, like derby & in-memory HSQL only allow access from within the JVM that is accessing the db. This presents a problem that you cannot run an external db admin tool to work with the db while your grails app is running. To get around this limitation I wrote small plugin (1 controller & 3 gsps) that can be used to admin the db within the running grails' app. 
There are three actions with the following urls described below...

/$appname$/dbUtil/info - Shows info about the db (tables/columns/column type, etc)
/$appname$/dbUtil/data - Outputs all the actual data alphabetically by table
/$appname$/dbUtil/sql - Allows free-form select/update/delete commands

Note: I've included commons-dbutils-1.1 from apache in the lib directory.

IMPORTANT: You will definitely want to protect this controller's url from unauthorized access since the sql action can be used to do just about anything.

DbutilController.groovy
data.gsp
info.gsp
sql.gsp'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/DbUtil+Plugin"

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
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
