class ForwardFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testForwardWithParams() {
        get('/simple/testForwardWithParams')
        assertStatus 200
        assertContentContains '<title>Simple GSP page</title>'
        assertContentContains 'params = bar'
    }
}
