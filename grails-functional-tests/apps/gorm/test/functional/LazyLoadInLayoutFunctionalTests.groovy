class LazyLoadInLayoutFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testLazyLoadingInLayouts() {
        // test lazy loading in layouts. An exception will be thrown if it is not working so we just test that the page renders properly
        get('/child/testLazyLoadInLayout')
        assertStatus 200
        assertContentContains '<p>joy</p>'
    }
}
