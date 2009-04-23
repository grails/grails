class TaglibOverrideFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testOverrideCoreTag() {
        get('/')
        assertStatus 200
        assertContentContains '<p>Message: overriden</p>'
    }
}
