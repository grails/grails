package org.grails.wiki

import org.grails.content.Content
import org.grails.content.Version

class WikiPage extends Content {

    def wikiPageService

	Version createVersion() {
        getWikiPageService().createVersion(this)
    }
	
	static hasMany = [versions:Version]

    static searchable = [only: ['body', 'title']]

    static mapping = {
        cache true
    }

	static constraints = {
		title(blank:false, matches:/[^\/\\]+/)
		body(blank:false)
	}

    String toString() {
        body
    }

}