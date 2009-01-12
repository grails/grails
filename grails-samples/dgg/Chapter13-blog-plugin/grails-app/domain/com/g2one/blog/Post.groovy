package com.g2one.blog

class Post {
	String title
	String body
	Date dateCreated
	Date lastUpdated
	
	static constraints = {
		title blank:false
		body type:"text", blank:false
	}
}
