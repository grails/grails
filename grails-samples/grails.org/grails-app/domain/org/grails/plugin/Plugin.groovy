package org.grails.plugin

import org.grails.wiki.WikiPage
import org.grails.taggable.Taggable
import org.grails.comments.Commentable
import org.grails.rateable.Rateable
/*
 * author: Matthew Taylor
 */
class Plugin implements Taggable, Commentable, Rateable {

    static final def WIKIS = ['installation','description','faq','screenshots']
    static final def VERSION_PATTERN = /^(\d{1,}(\.\d+)*)(-\w*$)?/

    def cacheService
    def pluginService
    
    String name
    String title
    WikiPage description
    WikiPage installation
    WikiPage faq
    WikiPage screenshots
    String author
    String authorEmail
    String currentRelease
    String documentationUrl
    String downloadUrl
    String grailsVersion        // version it was developed against
    Boolean official = false    // specifies SpringSource support
    Number avgRating
    Date dateCreated
    Date lastUpdated
    Date lastReleased

    static searchable = {
        only = [
            'name', 'title', 'author', 'authorEmail',
            'installation','description','faq','screenshots'
        ]
        description component: true
        installation component: true
        faq component: true
        screenshots component: true
    }

    static transients = ['avgRating','official']

    static constraints = {
        name unique: true
        description nullable: true
        installation nullable: true
        faq nullable: true
        screenshots nullable: true
        author nullable: true
        grailsVersion nullable:true, blank:true, maxLength:16
        lastReleased nullable:true
        currentRelease matches: VERSION_PATTERN
    }

    static mapping = {
        cache 'nonstrict-read-write'
    }

    def getOfficial() {
        authorEmail.trim().endsWith('@springsource.com') || authorEmail.trim().endsWith('@g2one.com')
    }

    def onAddComment = { comment ->
        cacheService.flushWikiCache()
    }

    def isNewerThan(version) {
        pluginService.compareVersions(currentRelease, version) > 0
    }

    String toString() {
        "$name : $title"
    }
}