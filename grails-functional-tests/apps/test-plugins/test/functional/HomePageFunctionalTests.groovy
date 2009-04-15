class HomePageFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    /**
     * Tests that resources from the app and plugins are copied on to
     * the classpath.
     */
    void testResources() {
        get "/home/index" 
        assertStatus 200
        assertContentStrict "OK" 
    }
}
