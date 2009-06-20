package org.grails.comments

class CommentService {

    boolean transactional = true

    def getLatestComments(type) {
        CommentLink.withCriteria {
			projections { property "comment" }
            eq 'type', type
            comment {
                order('dateCreated', 'desc')
            }
            maxResults PORTAL_MAX_RESULTS
			cache true
        }
    }
}
