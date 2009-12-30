class CreateControllerTestCase extends AbstractCliTestCase {
    void testInteractive() {
        execute([ "create-controller" ])
        enterInput "Dummy"
        
        assertEquals 0, waitForProcess()
    }
}
