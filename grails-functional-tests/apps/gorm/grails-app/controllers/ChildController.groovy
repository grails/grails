class ChildController {

	def testLazyLoadInLayout = {
		def t = Test.findByName("bob")
		
		[test:t]
	}

	def validator = {
		def validator = grailsApplication.getDomainClass("Test")?.validator

		render "Validator = ${validator?.class?.name}"
	}

}