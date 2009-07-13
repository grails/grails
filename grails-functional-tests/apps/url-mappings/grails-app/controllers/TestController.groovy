class TestController {

    def index = { 
		render "working"
	}
	
	def testAbsolutePaths = {}
	
	def testExtension = {
		render "${params.file}.${params.ext}"
	}
}
