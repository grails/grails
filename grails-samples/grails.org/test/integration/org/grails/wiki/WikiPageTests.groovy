package org.grails.wiki

import grails.test.GrailsUnitTestCase
import org.grails.comments.Comment
import org.grails.auth.User
/*
 * author: Matthew Taylor
 */
class WikiPageTests extends GroovyTestCase {

    void testCacheIsClearedOnAddComment() {

        def wiki = new WikiPage(title:'sam i am')
        def flushed = false
        wiki.cacheService = [flushWikiCache:{ -> flushed = true}]

        wiki.onAddComment(null)

        assertTrue "Wiki cache was not flushed onAddComment()", flushed

    }

}