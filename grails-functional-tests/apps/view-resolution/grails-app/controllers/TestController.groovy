class TestController {

    def testRelativeView = { 
		render view:"simple", model:[foo:"bar"]
	}

    def testAbsoluteView = { 
		render view:"/test/absolute", model:[foo:"bar"]
	}

    def testDefaultView = { 
		[foo:"bar"]
	}

	// TODO: nested relative views not supported yet. See GRAILS-4204
    def testNestedRelativeView = { 
		render view:"local/simple", model:[foo:"bar"]
	}

}
