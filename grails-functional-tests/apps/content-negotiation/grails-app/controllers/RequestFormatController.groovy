class RequestFormatController {

	def testFormat = {
		render "request format = ${request.format}"
	}

	def testWithFormat = {
		withFormat {
			html {
				render "request format html"
			}
			xml {
				render "request format xml"
			}
		}
	}
}