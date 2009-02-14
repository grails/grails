package org.grails.wiki

import org.grails.content.Version

class WikiPageService {

    boolean transactional = true

    def createVersion(wikiPage) {
        def verObject = new Version()
        verObject.number = conjureNewVersionNumber(wikiPage)
        verObject.current = wikiPage
        println "Creating version with number ${verObject.number}"
        verObject.title = wikiPage.title
        verObject.body = wikiPage.body
        verObject
    }

    private def conjureNewVersionNumber(wiki) {
        if (!wiki.versions) return 1
        wiki.versions*.number.sort()[-1] + 1
    }
}
