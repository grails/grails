class PluginLayoutsFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testPluginLayoutFoundByPluginView() {
        // Here call get(uri) or post(uri) to start the session
        // and then use the custom assertXXXX calls etc to check the response
        //
        get('/dbUtil/data')
        assertStatus 200
		// test some content from the layout
        assertContentContains '<a href="/layouts/dbUtil/data">Display Data</a>'
    }

	void testPluginLayoutFoundByApplicationView() {
        get('/layout/pluginView')
        assertStatus 200
		// test some content from the layout
        assertContentContains '<a href="/layouts/dbUtil/data">Display Data</a>'
		
	}
	
	void testApplyLayout() {
		get("/layout/testApplyLayout")
		assertStatus 200
		
		assertContentContains '<h1>Test Layout</h1>'
	}
	
	void testApplyLayoutWithPluginView() {
        get('/layout/testApplyLayoutPluginView')
        assertStatus 200
		// test some content from the layout
        assertContentContains '<a href="/layouts/dbUtil/data">Display Data</a>'
		
	}
	
	void testApplyLayoutAndSecondLayout() {
        get('/layout/testTwoLevelLayout')
        assertStatus 200
		// test some content from the layout
        assertContentContains '<a href="/layouts/dbUtil/data">Display Data</a>'
		assertContentContains '<h1>Test Two Level Layout</h1>'
		
	}
	
	
}
