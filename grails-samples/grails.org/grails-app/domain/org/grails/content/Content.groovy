package org.grails.content

import org.grails.comments.*

class Content implements Serializable, Commentable {
	String title
	String body
    Boolean locked = false
	Date dateCreated
	Date lastUpdated

	static mapping = {
		body type:"text"
	}
	static constraints = {
		title(blank:false)
		body(blank:true)
	}
}