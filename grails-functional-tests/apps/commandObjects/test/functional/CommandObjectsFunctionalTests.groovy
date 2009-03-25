class CommandObjectsFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testCommandObjects() {
         get('/test/testCommand?name=fred&age=45')
         assertStatus 200
         assertContentContains 'name: fred, age:45'
    }

	void testCommandObjectsAdditionalParams() {
        get('/test/testCommand?name=fred&age=45&x=23&y=343')
        assertStatus 200
        assertContentContains 'name: fred, age:45'
		
	}
}
