class CommandObjectsFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testCommandObjects() {
         get('/test/testCommand?name=fred&age=45')
         assertStatus 200
         assertContentContains 'name: fred, age:45'
    }

/* TODO - uncomment
	void testCommandObjectsAdditionalParams() {
        get('/test/testCommand?name=fred&age=45&x=23&y=343')
        assertStatus 200
        assertContentContains 'name: fred, age:45'		

        get('/test/testCommand?name=fred&age=45&name.x=23&name.y=343')
        assertStatus 200
        assertContentContains 'name: fred, age:45'		

	}
*/

	void testCommandObjectsWithNestedObject() {
        get('/test/testCommandWithNestedValues?name=fred&age=45&x=23&y=343&nested.value=test')
        assertStatus 200
        assertContentContains 'name: fred, age: 45, nested.value: test'		

        get('/test/testCommandWithNestedValues?name=fred&age=45&x=23&y=343&nested.value=test&nested.x=345')
        assertStatus 200
        assertContentContains 'name: fred, age: 45, nested.value: test'		

	}	
	
}
