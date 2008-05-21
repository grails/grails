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

        assertOutputEquals "<a name=\"Hello\"></a><h1>Hello</h1>", template

         template = '<wiki:text>h2. Hello</wiki:text>'

        assertOutputEquals "<a name=\"Hello\"></a><h2>Hello</h2>", template

    }

    void testBold() {
        def template = '<wiki:text>*Hello*</wiki:text>'

        assertOutputEquals '<strong class="bold">Hello</strong>', template

    }


    void testLinksForExistantPages() {

        def page = new WikiPage(title:"Test Page", body:"stuff")
        assert page.save(flush:true)
        
        def template = '<wiki:text>My Link [Test Page]</wiki:text>'

        assertOutputEquals 'My Link <a href="/Test+Page" class="pageLink">Test Page</a>', template

    }

    void testLinksForExistantPagesWithAnchors() {

        def page = new WikiPage(title:"Test Page", body:"stuff")
        assert page.save(flush:true)

        def template = '<wiki:text>My Link [Test Page|Test Page#MyAnchor]</wiki:text>'

        assertOutputEquals 'My Link <a href="/Test+Page#MyAnchor" class="pageLink">Test Page</a>', template

    }

    void testLinksForNonExistantPages() {
       def template = '<wiki:text>My Link [Random Page]</wiki:text>'

        assertOutputEquals 'My Link <a href="./create/Random+Page" class="createPageLink">Random Page (+)</a>', template        
    }
}