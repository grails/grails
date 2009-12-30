package org.grails.wiki

import org.grails.content.Content
import org.grails.content.Version

class WikiPage extends Content {

    transient cacheService
    
	Version createVersion() {
        def verObject = new Version(number:version, current:this)
        verObject.title = title
        verObject.body = body
        return verObject
    }
	
	static hasMany = [versions:Version]

    static searchable = [only: ['body', 'title']]

	static constraints = {
		title(blank:false, matches:/[^\/\\]+/)
		body(blank:true)
	}

    def onAddComment = { comment ->
        cacheService?.flushWikiCache()
    }

    String toString() {
        body
    }

}