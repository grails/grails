package org.grails.wiki

import grails.test.GroovyPagesTestCase

/**
* @author Graeme Rocher
* @since 1.0
*
* Created: Feb 27, 2008
*/
class WikiTagLibTests extends GroovyPagesTestCase {

    void testHeadTags() {
        def template = '<wiki:text>h1. Hello</wiki:text>'

        assertOutputEquals "<h1>Hello</h1>", template

         template = '<wiki:text>h2. Hello</wiki:text>'

        assertOutputEquals "<h2>Hello</h2>", template 

    }

    void testBold() {
        def template = '<wiki:text>*Hello*</wiki:text>'

        assertOutputEquals '<strong class="bold">Hello</strong>', template

    }


    void testLinksForExistantPages() {

        def page = new WikiPage(title:"Test Page", body:"stuff")
        assert page.save(flush:true)
        
        def template = '<wiki:text>My Link [Test Page]</wiki:text>'

        assertOutputEquals 'My Link <a href="/Test+Page" class="Test+Page">Test Page</a>', template

    }

    void testLinksForNonExistantPages() {
       def template = '<wiki:text>My Link [Random Page]</wiki:text>'

        assertOutputEquals 'My Link <a href="./create/Random+Page" class="Random+Page">Random Page</a>', template        
    }
}