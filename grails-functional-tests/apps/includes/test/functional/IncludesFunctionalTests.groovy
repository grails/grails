class IncludesFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testIncludeWithParams() {
        get('/simple/testInclude')
        assertStatus 200
        assertContentContains '<title>Simple GSP page</title>'
        assertContentContains 'params = bar'
    }
}
