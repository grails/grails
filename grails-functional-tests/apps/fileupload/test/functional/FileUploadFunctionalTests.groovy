class FileUploadFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testUploadFile() {
        get('/upload/index')
        assertStatus 200
        
		form('myForm') {
		  myFile.data = "some text".bytes
		  myFile.contentType = "text/plain"
		  click "Upload"
		}		
		
        assertStatus 200
		assertContentContains "upload = true"
    }
}
