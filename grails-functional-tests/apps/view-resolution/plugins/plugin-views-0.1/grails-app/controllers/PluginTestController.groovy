class PluginTestController {

    def testRelativeView = { 
		render view:"simple", model:[foo:"bar"]
	}

    def testAbsoluteView = { 
		render view:"/pluginTest/absolute", model:[foo:"bar"]
	}

    def testDefaultView = { 
		[foo:"bar"]
	}
	
	def testOverridenView = {
		render view:"overriden", model:[foo:"bar"]
	}

	// TODO: nested relative views not supported yet. See GRAILS-4204
    def testNestedRelativeView = { 
		render view:"local/simple", model:[foo:"bar"]
	}
}
