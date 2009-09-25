class HelpTestCase extends AbstractCliTestCase {
    void testNoArgs() {
        execute([ "help" ])

        assertEquals 0, waitForProcess()
        assertTrue output?.contains("create-controller")
        verifyHeader()
    }
}
