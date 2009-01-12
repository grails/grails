package com.g2one.gtunes

import org.springframework.webflow.execution.repository.NoSuchFlowExecutionException

class ErrorsController {
	def index = {
		if(request.exception?.cause instanceof NoSuchFlowExecutionException) {
			redirect(controller:"store", action:"shop")
		}
		else {
			render(view:"/error", model:[exception:request.exception])
		}
	}
}