class ChildController {

	def testLazyLoadInLayout = {
		def t = Test.findByName("bob")
		
		[test:t]
	}

}