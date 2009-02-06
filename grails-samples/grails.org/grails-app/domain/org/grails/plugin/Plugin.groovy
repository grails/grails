package org.grails.plugin

import org.grails.wiki.WikiPage
import org.grails.content.Version
/*
 * author: Matthew Taylor
 */
class Plugin extends WikiPage {

    String description
    String installation
    String faq
    String author
    String authorEmail
    String screenshots
    Boolean official // specifies SpringSource support
    Number avgRating

    static hasMany = [tags:Tag, ratings:Rating]

    static transients = ['avgRating']

    static constraints = {
        title(unique:true)
        description(nullable: true)
        installation(nullable: true)
        faq(nullable: true)
        screenshots(nullable: true)
        author(nullable: true)
        authorEmail(email:true, blank:false)
    }

    Version createVersion() {
        def verObject = super.createVersion()
        verObject.description = description
        verObject.installation = installation
        verObject.faq = faq
        verObject.screenshots = screenshots
        verObject.stats = stats
        verObject.tags = tags
        verObject.ratings = ratings
    }

    def getAvgRating() {
        ratings*.stars.sum() / ratings.size()
    }

    // set the content body same as description
    void setDescription(String desc) {
        this.@description = desc
        this.body = desc
    }
}