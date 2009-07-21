class BindAssociationToNullFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testBindAssociationToNull() {
        get('/book/create')
        assertStatus 200
        assertContentContains '<title>Create Book</title>'

		// defaults to no selection
		form('createForm') {
		      title = "Test Book"
		      click "create"
		}
		
        assertStatus 200
        assertContentContains '<title>Show Book</title>'
        assertContentContains 'Test Book'

		// now test update
		form('showForm') {
		      click "_action_Edit"
		}
		
        assertStatus 200
        assertContentContains '<title>Edit Book</title>'
		
	
		form('editForm') {
			  title = "Changed Book"
		      click "_action_Update"
		}
		
        assertStatus 200
        assertContentContains '<title>Show Book</title>'
        assertContentContains 'Changed Book'
		
    }
}
