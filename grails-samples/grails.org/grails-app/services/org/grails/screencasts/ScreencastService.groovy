package org.grails.screencasts

class ScreencastService {

    boolean transactional = true

    def getLatestScreencastId() {
        Screencast.withCriteria {
            order 'dateCreated', 'desc'
            maxResults 1
            projections {
                property 'id'
            }
        }[0]
    }
}
