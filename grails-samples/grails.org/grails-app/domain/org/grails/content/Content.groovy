package org.grails.content

class Content implements Serializable {
	String title
	String body
	Date dateCreated
	Date lastUpdated
	
	static mapping = {
		body type:"text"
	}
	static constraints = {
		title(blank:false)
		body(blank:false)
	}
}