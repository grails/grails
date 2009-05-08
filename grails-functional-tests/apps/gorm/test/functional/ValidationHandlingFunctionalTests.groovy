class ValidationHandlingFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testBindingErrorHandling() {
        get('/test/create')
        assertStatus 200
        
		form("testForm") {
			name = "test"
			age = 25
			click "Create"
		}
		
		assertStatus 200		
		assertContentContains "Test 2 created"
		
		form("navForm") {
			click "_action_Edit"
		}
		
		assertStatus 200		
		assertContentContains "Edit Test"
		assertContentContains '<input type="text" id="name" name="name" value="test"/>'
		assertContentContains '<input type="text" id="age" name="age" value="25" />'		
		
		form("editForm") {
			name = "bad"
			age = "not a number"
			click "_action_Update"
		}
		
		assertStatus 200
		assertContentContains "Edit Test"		
		assertContentContains "Property age must be a valid number"
		
        get('/test/show/1')
		assertStatus 200
		assertContentContains '<td valign="top" class="value">test</td>'
		assertContentContains '<td valign="top" class="value">25</td>'		
		
		
    }
}
