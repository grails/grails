class CompileTestCase extends AbstractCliTestCase {
    protected void setUp() {
        workDir = new File(baseWorkDir, "app1")
    }

    void testNoArgs() {
        execute([ "compile" ])

        assertEquals 0, waitForProcess()
        verifyHeader()
        assertTrue output.contains("[groovyc] Compiling")
    }
}

