class SimpleController {

	def index = {}
	
	def testInclude = {}

	def testForwardWithParams = {
		forward(controller:"simple", action:"index", params:[foo:'bar'])
	}
}