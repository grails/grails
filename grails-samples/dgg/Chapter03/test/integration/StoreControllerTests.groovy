class StoreControllerTests extends GroovyTestCase {

    void testRenderHomePage() {
		def controller = new StoreController()
		controller.index()
		assertEquals "Welcome to the gTunes store!", controller.response.contentAsString
    }
}
