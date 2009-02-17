package org.grails.comment

import org.grails.auth.User

class Comment {
	User user
	String body
    Date dateCreated
	
	static constraints = {
		body(blank:false)
	}

}