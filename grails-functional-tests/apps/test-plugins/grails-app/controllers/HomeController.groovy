class HomeController {
    def index = {
        // Check that we can load various application and plugin provided
        // resources off the classpath.
        def cl = Thread.currentThread().contextClassLoader
        // App resources
        assert cl.getResource("app-hib/my-config.xml") != null
        assert cl.getResource("foo/bar.xml") != null
        assert cl.getResource("app-stuff/readme.txt") != null
        assert cl.getResource("org/grails/app-stuff/groovy-readme.txt") != null
        // Plugin resources
        assert cl.getResource("custom-hib/my-plugin-config.xml") != null
        assert cl.getResource("foo/plugin-bar.xml") != null
        assert cl.getResource("resources-plugin/readme.txt") != null
        assert cl.getResource("org/grails/resources-plugin/groovy-readme.txt") != null

        render "OK"
    }
}
