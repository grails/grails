class SpringMvcFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testCallSpringMvcController() {
        // Here call get(uri) or post(uri) to start the session
        // and then use the custom assertXXXX calls etc to check the response
        //
        get('/hello')
        assertStatus 200
        assertContentContains '<h1 id="hello">Hello Fred</h1>'
		// check layout applied
        assertContentContains '<img src="/spring/images/grails_logo.jpg" alt="Grails" />'
    }
}
