package org.grails.wiki

import org.grails.content.Content
import org.grails.content.Version

class WikiPage extends Content {
	


	Version createVersion() {       
        def verObject = new Version(number:version, current:this)
        verObject.title = title
        verObject.body = body
        return verObject
    }
	
	static hasMany = [versions:Version]

    static searchable = [only: ['body', 'title']]    

}