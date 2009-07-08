class ReadObjectObjectValueFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testReadConfigValues() {
        // Here call get(uri) or post(uri) to start the session
        // and then use the custom assertXXXX calls etc to check the response
        //
        get('/test/read')
        assertStatus 200
        assertContentContains 'one = first, two = second'
    }
}
