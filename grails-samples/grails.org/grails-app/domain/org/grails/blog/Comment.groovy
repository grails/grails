package org.grails.blog

class Comment {
	String poster
	String body
	String email
	
	static constraints = {
		poster(blank:false)
		email(email:true, nullable:true)
		body(blank:false)
	}
}