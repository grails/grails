class HelloControllerTest extends grails.util.WebTest {

    // Unlike unit tests, functional tests are often sequence dependent.
    // Specify that sequence here.
    void suite() {
        testHelloController()
        // add tests for more operations here
    }

    def testHelloController() {
        webtest('HelloController index test') {
            invoke      (url: 'hello/')
            verifyText  (text:'Hello world!')
        }
    }
}