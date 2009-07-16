class SpringTransactionController {
	
	def springConfiguredService
	
	def testOne = {
		render springConfiguredService.methodOne()
	}
	
	def testTwo = {
		render springConfiguredService.methodTwo()
	}

}