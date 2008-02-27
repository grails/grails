package org.grails.content

import org.grails.auth.User

class Version extends Content {
	Integer number
	Content current
    User author

    static mapping = {
        cache usage:'read-only'
    }
}