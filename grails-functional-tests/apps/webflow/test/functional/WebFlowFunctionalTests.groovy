class WebFlowFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testFlowMethodDispatch() {
        get('/testFlow/my')
        assertStatus 200
        assertContentContains '<title>Test Flow</title>'

		form("testForm") {
			click "_eventId_go"
		}
		
        assertStatus 200
        assertContentContains 'Flow Ended'
		
    }
}
