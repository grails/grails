class ListPluginsTestCase extends AbstractCliTestCase {
    private static final String CORE_PLUGINS_HEADER = "Plug-ins available in the core repository are listed below:"
    private static final String OTHER_PLUGINS_HEADER = "Plug-ins available in the default repository"

    void testNoArgs() {
        execute([ "list-plugins" ])

        assertEquals 0, waitForProcess()
        assertTrue output.contains(CORE_PLUGINS_HEADER)
        assertTrue output.contains(OTHER_PLUGINS_HEADER)
        verifyIsCorePlugin("hibernate")
        verifyIsCorePlugin("tomcat")
        verifyIsCorePlugin("webflow")
        verifyIsOtherPlugin("grails-ui")
        verifyIsOtherPlugin("gwt")
        verifyHeader()
    }

    private void verifyIsCorePlugin(String pluginName) {
        def coreIndex = output.indexOf(CORE_PLUGINS_HEADER)
        def otherIndex = output.indexOf(OTHER_PLUGINS_HEADER)
        def pluginIndex = output.indexOf(pluginName)
        assertTrue "'${pluginName} is not a core plugin.", pluginIndex > coreIndex && pluginIndex < otherIndex
    }

    private void verifyIsOtherPlugin(String pluginName) {
        def otherIndex = output.indexOf(OTHER_PLUGINS_HEADER)
        def pluginIndex = output.indexOf(pluginName)
        assertTrue "'${pluginName} is not a plugin or is a core plugin.", pluginIndex > otherIndex
    }
}

