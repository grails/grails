class SpecialCharactersFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testSpecialCharactersInView() {
        // Here call get(uri) or post(uri) to start the session
        // and then use the custom assertXXXX calls etc to check the response
        //
        get('/error/pageNotFound')
        assertStatus 200
        assertContentContains 'Précédent'
        assertContentContains 'Anterioarăîâşţ'
    }

	void testSpecialCharactersInPageNotFound() {
        get('/error/notThere')
        assertStatus 404
        assertContentContains 'Précédent'
        assertContentContains 'Anterioarăîâşţ'		
	}
}
