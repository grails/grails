class MainFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    /**
     * Tests that localised text is loaded from properties files that
     * reside in a package structure, i.e. in sub-directories of
     * <tt>grails-app/i18n</tt>.
     */
    void testPropertiesInPackage() {
        // Here call get(uri) or post(uri) to start the session
        // and then use the custom assertXXXX calls etc to check the response
        //
        get "/main"
        assertStatus 200
        assertTitleContains "The main page"

        get "/main?lang=fr"
        assertStatus 200
        assertTitleContains "La page principale"
    }
}
