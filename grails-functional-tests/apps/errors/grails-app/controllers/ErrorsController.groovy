class ErrorsController {

	def standard = {
		throw new Exception("bad")
	}
	
	def declarative = {
		throw new IllegalStateException("bad")
	}
	
	def layout = {
		throw new IllegalArgumentException("bad")
	}
	
	def displayLayout = {}
	
	def customException = {
		render "Exception was ${request.exception.class.name}"
	}

	def gstringTagError = {}
	
	def tagExpressionError = {}
	
	def regularExpressionError = {}
	
	def templateError = {}
	
	def internalTagError = {}
	
	def invalidGormMethod = {
		[books:Book.findAllByRubbish("yes")]
	}
	
	def invalidCriteria = {
		def results = Book.withCriteria {
			eq('title', 'Good')
			eq('rubbish', 'Bad')
		}
		[books:results]
	}
	
	def redirectAction = {
		redirect(action:"pageNotFound")
	}
	
	def pageNotFound = {
		render "not there"
	}
	
	def warDeployed = {
		render "war=${grailsApplication.warDeployed}"
	}
	
}