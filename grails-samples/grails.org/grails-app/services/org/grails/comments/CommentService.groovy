package org.grails.comments

class CommentService {

    boolean transactional = true

    def getLatestComments(type, max) {
        CommentLink.withCriteria {
			projections { property "comment" }
            eq 'type', type
            comment {
                order('dateCreated', 'desc')
            }
            maxResults max
			cache true
        }
    }
}
