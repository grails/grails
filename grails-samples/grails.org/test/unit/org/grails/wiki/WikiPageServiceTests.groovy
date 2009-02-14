package org.grails.wiki

import grails.test.*
import org.grails.content.Version

class WikiPageServiceTests extends GrailsUnitTestCase {

    void testGetWikiPageVersion() {
        mockDomain(Version)
        mockDomain(WikiPage)

        def versions = [new Version(number:1),new Version(number:2)]

        def page = new WikiPage()
        page.versions = versions

        WikiPageService service = new WikiPageService()

        def newVersion = service.createVersion(page)

        assertTrue newVersion instanceof Version
        assertEquals 3, newVersion.number
    }
}
