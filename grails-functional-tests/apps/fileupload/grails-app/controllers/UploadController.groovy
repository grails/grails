class UploadController {

	def index = {}
	
	def testUpload = {
		def file = request.getFile("myFile")
		
		render "upload = ${file!=null}"
	}

}