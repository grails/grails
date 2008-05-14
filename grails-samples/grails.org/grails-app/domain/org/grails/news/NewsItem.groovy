package org.grails.news

import org.grails.content.Content
import org.grails.auth.User

/**
* @author Graeme Rocher
* @since 1.0
*
* Created: Feb 27, 2008
*/
class NewsItem extends Content {
    User author

    static constraints = {
        body(size:1..300)
    }


    static mapping = {
        cache true
    }
}