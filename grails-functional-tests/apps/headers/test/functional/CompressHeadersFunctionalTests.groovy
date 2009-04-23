class CompressHeadersFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testCompressedOutput() {
        // test for GRAILS-4183: If this headers are broken then garbbled output will result
        get('/')
        assertStatus 200
        assertContentContains 'Welcome to Grails'
    }
}
