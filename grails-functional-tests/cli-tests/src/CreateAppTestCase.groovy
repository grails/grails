class CreateAppTestCase extends AbstractCliTestCase {
    void testNoArgs() {
        execute([ "create-app" ])
//        Thread.sleep(10000)
        enterInput "app1"

        assertEquals 0, waitForProcess()
        verifyHeader()
        verifyApp("app1")
        assertTrue output.contains("Application name not specified. Please enter:")
        assertTrue output.contains("Created Grails Application at ${baseWorkDir.absolutePath}/app1")
    }

    void testWithAppName() {
        String appName = "app2"
        execute([ "create-app", appName])

        assertEquals 0, waitForProcess()
        verifyHeader()
        verifyApp(appName)
        assertFalse output.contains("Application name not specified. Please enter:")
        assertTrue output.contains("Created Grails Application at ${baseWorkDir.absolutePath}/${appName}")
    }

    private void verifyApp(String name) {
        final File appDir = new File(baseWorkDir, name)

        assertTrue appDir.exists()
        assertTrue new File(appDir, "${name}.iml").exists()
        assertTrue new File(appDir, "${name}.ipr").exists()
        assertTrue new File(appDir, "${name}.iws").exists()
        assertTrue new File(appDir, "${name}.launch").exists()
        assertTrue new File(appDir, "${name}-test.launch").exists()
        assertTrue new File(appDir, "${name}.tmproj").exists()
        assertTrue new File(appDir, "application.properties").exists()
        assertTrue new File(appDir, "build.xml").exists()
        assertTrue new File(appDir, "ivysettings.xml").exists()
        assertTrue new File(appDir, "ivy.xml").exists()
        assertTrue new File(appDir, "grails-app").exists()
        assertTrue new File(appDir, "lib").exists()
        assertTrue new File(appDir, "scripts").exists()
        assertTrue new File(appDir, "src").exists()
        assertTrue new File(appDir, "test").exists()
        assertTrue new File(appDir, "web-app").exists()
    }
}
