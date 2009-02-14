package org.grails.comment

import org.grails.content.Content
import org.grails.auth.User

class Comment {
	User user
	String body
	
	static constraints = {
		body(blank:false)
	}

}