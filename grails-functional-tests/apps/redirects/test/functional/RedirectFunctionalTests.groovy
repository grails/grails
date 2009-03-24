class RedirectFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testRedirectWithParams() {
        get('/redirect/form')
        assertStatus 200
		form('searchForm') {
		      q = "#grails"
		      click "Search"
		}
		
		assertContentContains "query = #grails"
    }

	void testRedirectWithDuplicateParams() {
		get('/redirect/testRedirectWithDuplicateParams')
        assertStatus 200		
		assertContentContains "query = [one,two]"		
	}
	
	void testRedirectWithDuplicateParamsArray() {
		get('/redirect/testRedirectWithDuplicateParamsArray')
        assertStatus 200		
		assertContentContains "query = [one,two]"		
		
	}
}
