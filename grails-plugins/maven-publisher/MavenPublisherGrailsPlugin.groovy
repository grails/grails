class MavenPublisherGrailsPlugin {
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Graeme Rocher"
    def authorEmail = "grocher@vmware.com"
    def title = "A plugin that allows you to publish and/or resolve Grails application and plugins to Maven repositories without needing to use Maven directly"
    def description = '''\\
Brief description of the plugin.
'''

    def documentation = "http://grails.org/plugin/maven-publisher"
}
