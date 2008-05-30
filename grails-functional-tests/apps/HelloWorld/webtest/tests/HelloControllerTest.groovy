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
    	def originalText = "Hello world!"
       	def newText = "Adios!"
       	def f = new File("grails-app/controllers/HelloController.groovy")
    	def originalCode = f.text
    	def newCode = originalCode.replaceAll(originalText, newText)
    	assert originalCode != newCode

    	webtest('Controller reloading') {
    		group(description: "Gets the page and check original output") {
	            invoke      (url: 'hello/')
	            verifyText text: "$originalText"
	            not {
	            	verifyText text: "$newText"
	            }
    		}

            groovy description: "Modify the controller", """
def f = new File("$f")
f.withWriter { writer ->
    writer << '''$newCode'''
}
"""
    		group(description: "Gets the page again and check new output") {
	            invoke      (url: 'hello/')
            	verifyText text: "$newText"
	            not {
    	            verifyText text: "$originalText"
	            }
    		}
        }

        // Reset the controller back to the way it was.
        f.withWriter { writer ->
            writer << originalCode
        }
    }

    def testReloadingControllerView() {
    	def originalText = "Hello from myTag!"
       	def newText = "new output of myTag"
       	def f = new File("grails-app/taglib/FooTagLib.groovy")
    	def originalCode = f.text
    	def newCode = originalCode.replaceAll(originalText, newText)
    	assert originalCode != newCode

    	webtest('TagLib reloading') {
    		group(description: "Gets the page and check original tag output") {
	            invoke      (url: 'hello/reloadTest')
	            verifyTitle  (text: 'TagLib reloading test')
	            verifyText text: "$originalText"
	            not {
	            	verifyText text: "$newText"
	            }
    		}

            groovy description: "Modify the taglib", """
def f = new File("$f")
f.withWriter { writer ->
    writer << '''$newCode'''
}
"""
    		group(description: "Gets the page again and check new tag output") {
	            invoke      (url: 'hello/reloadTest')
	            verifyTitle  (text: 'TagLib reloading test')
            	verifyText text: "$newText"
	            not {
    	            verifyText text: "$originalText"
	            }
    		}
        }

        // Reset the tag lib back to the way it was.
        f.withWriter { writer ->
            writer << originalCode
        }
    }

    def testReloadingStandaloneView() {
    	def originalText = "Hello from myTag!"
       	def newText = "new output of myTag"
       	def f = new File("grails-app/taglib/FooTagLib.groovy")
    	def originalCode = f.text
    	def newCode = originalCode.replaceAll(originalText, newText)
    	assert originalCode != newCode

    	webtest('TagLib reloading in with standalone view') {
    		group(description: "Gets the page and check original tag output") {
	            invoke      (url: 'tagUsage.gsp')
	            verifyTitle  (text: 'For taglib tests')
	            verifyText text: "$originalText"
	            not {
	            	verifyText text: "$newText"
	            }
    		}
    		
    		groovy description: "Modify the taglib", """
def f = new File("$f")
f.withWriter { writer ->
    writer << '''$newCode'''
}
"""
    		group(description: "Gets the page again and check new tag output") {
	            invoke      (url: 'tagUsage.gsp')
	            verifyTitle  (text: 'For taglib tests')
            	verifyText text: "$newText"
	            not {
    	            verifyText text: "$originalText"
	            }
    		}
        }

        // Reset the tag lib back to the way it was.
        f.withWriter { writer ->
            writer << originalCode
        }
    }
}
