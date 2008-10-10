class HelloControllerTest extends grails.util.WebTest {
    // Unlike unit tests, functional tests are often sequence dependent.
    // Specify that sequence here.
    void suite() {
        testHelloController()
        testReloadingController()
        testReloadingControllerView()
        testReloadingStandaloneView()
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

    def testReloadingController() {
        testReloading(
                new File("grails-app/controllers/HelloController.groovy"),
                "Controller reloading",
                "Hello world!",
                "Adios!",
                "hello/",
                "")
    }

    def testReloadingControllerView() {
        testReloading(
                new File("grails-app/taglib/FooTagLib.groovy"),
                "TagLib reloading with controller view",
                "Hello from myTag!",
                "new output of myTag",
                "hello/reloadTest",
                "TagLib reloading test")
    }

    def testReloadingStandaloneView() {
        testReloading(
                new File("grails-app/taglib/FooTagLib.groovy"),
                "TagLib reloading with standalone view",
                "Hello from myTag!",
                "new output of myTag",
                "tagUsage.gsp",
                "For taglib tests")
    }

    private void testReloading(
            File artefactFile,
            String webtestDescription,
            String originalText,
            String newText,
            String invokeUrl,
            String pageTitle) {
    	def originalCode = artefactFile.text 
		try {
    	def newCode = originalCode.replaceAll(originalText, newText)
    	assert originalCode != newCode

    	webtest(webtestDescription) {
    		group(description: "Gets the page and checks original output") {
	            invoke      (url: invokeUrl)
	            verifyTitle (text: pageTitle)
	            verifyText  (text: "$originalText")
	            not {
	            	verifyText (text: "$newText")
	            }
    		}

    		groovy description: "Modify the artefact", """
// Ensures that the file appears modified on systems that only
// have a granularity of a second on their file timestamps.
Thread.sleep(1000)

def f = new File("$artefactFile")
f.withWriter { writer ->
    writer << '''$newCode'''
}

Thread.sleep(2000)
"""
    		group(description: "Gets the page again and checks new output") {
	            invoke      (url: invokeUrl)
	            verifyTitle (text: pageTitle)
                verifyText  (text: "$newText")
	            not {
                    verifyText (text: "$originalText")
	            }
    		}
        }
			
		}
		finally {
	        // Reset the tag lib back to the way it was. The "sleep()"
	        // ensures that the file appears modified on systems that only
	        // have a granularity of a second on their file timestamps.
	        Thread.sleep(1000)
	        artefactFile.withWriter { writer ->
	            writer << originalCode
	        }
	        Thread.sleep(2000)			
		}
    }
}
