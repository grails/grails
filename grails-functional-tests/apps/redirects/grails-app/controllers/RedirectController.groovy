class RedirectController {

	def form = {
		
	}
	
	def testRedirectWithParams = {
		redirect(action:"output", params:params)
	}
	
	def testRedirectWithDuplicateParams = {
		redirect(action:"output", params:[q:['one','two']])
	}

	def testRedirectWithDuplicateParamsArray = {
		redirect(action:"output", params:[q:['one','two'] as String[]])
	}
	
	def output = {
		render "query = ${params.q}"
	}

}