package org.grails.comment

import org.grails.content.Content

class Comment {
	String poster
	String body
	String email
    Content parent
	
	static constraints = {
		poster(blank:false)
		email(email:true, nullable:true)
		body(blank:false)
	}
}