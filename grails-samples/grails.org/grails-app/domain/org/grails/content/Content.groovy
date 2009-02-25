package org.grails.content

import org.grails.comment.Comment

class Content implements Serializable {
	String title
	String body
    Boolean locked = false
	Date dateCreated
	Date lastUpdated

    static hasMany = [comments:Comment]
    
	static mapping = {
		body type:"text"
	}
	static constraints = {
		title(blank:false)
		body(blank:true)
	}
}