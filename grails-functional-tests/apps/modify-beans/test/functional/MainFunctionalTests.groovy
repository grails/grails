class MainFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testSomeWebsiteFeature() {
        // Here call get(uri) or post(uri) to start the session
        // and then use the custom assertXXXX calls etc to check the response
        //
        // get('/something')
        // assertStatus 200
        // assertContentContains 'the expected text'
        get "/"
        assertContentContains "Welcome to Grails"

        get "/?language22=fr"
        assertContentContains "Bienvenue à Rails"
    }
}
