class HelloControllerTest extends grails.util.WebTest {
    // Unlike unit tests, functional tests are often sequence dependent.
    // Specify that sequence here.
    void suite() {
        testHelloController()
        // add tests for more operations here
    }

    def testHelloController() {
        webtest('HelloController index test') {
            invoke     (url: 'hello/')
            verifyText (text:'Hello world!')

            // Test simple URL mapping.
            invoke     (url:  'mapping')
            verifyText (text: 'Controller: hello, action: mapping, other: test string')
            invoke     (url:  'mapping2')
            verifyText (text: 'Controller: hello, action: mapping, other: test string')
            invoke     (url:  'hello/mapping')
            verifyText (text: 'Controller: hello, action: mapping, other: test string')

            // Test error handling.
            groovy("step.context.webClient.throwExceptionOnFailingStatusCode = false")

            // This URL sends an "internal server error", which maps to
            // the "show" action of the errors controller.
            invoke     (url:  'hello/bad')
            verifyText (text: 'Internal server error: 1001')

            // This URL sends a custom 414 error, which maps to a
            // standalone view.
            invoke     (url:  'hello/error414')
            verifyText (text: 'But I still haven\'t found what I\'m looking for...')

            // This URL sends a custom 444 error, which maps to the
            // "kaput" view of the hello controller.
            invoke     (url:  'hello/error444')
            verifyText (text: 'Something is kaput!')

            groovy("step.context.webClient.throwExceptionOnFailingStatusCode = true")
        }
    }
}