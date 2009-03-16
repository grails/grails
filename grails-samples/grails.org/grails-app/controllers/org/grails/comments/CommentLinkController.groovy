

package org.grails.comments

class CommentLinkController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        def list = CommentLink.list(params)
        list.each {
            def userClass = grailsApplication.classLoader.loadClass(it.comment.posterClass)
            def user = userClass.get(it.comment.posterId)
            it.metaClass.getPoster = {-> user }
        }
        [ commentLinkInstanceList: list, commentLinkInstanceTotal: CommentLink.count() ]
    }

    def delete = {
        def commentLink = CommentLink.get(params.id)
        def comment = CommentLink.createCriteria().get {
            projections {
                property 'comment'
            }
            idEq(params.id.toLong())
        }
        println "criteria comment: $comment.id"
        if(comment) {
            try {
                // TODO: CommentLink belongsTo Comment, so I should only need to call delete() on the comment here, but
                //       I must call it on the commentLink first.  I can't get it working otherwise
                commentLink.delete()
                comment.delete()
                flash.message = "Comment for CommentLink ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "CommentLink ${params.id} could not be deleted"
                redirect(action:list)
            }
        }
        else {
            flash.message = "CommentLink not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def commentInstance = Comment.get( params.id )

        if(!commentInstance) {
            flash.message = "Comment not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ commentInstance : commentInstance ]
        }
    }

    def update = {
        def commentInstance = Comment.get( params.id )
        if(commentInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(commentInstance.version > version) {

                    commentInstance.errors.rejectValue("version", "comment.optimistic.locking.failure", "Another user has updated this Comment while you were editing.")
                    render(view:'edit',model:[commentInstance:commentInstance])
                    return
                }
            }
            commentInstance.properties = params
            if(!commentInstance.hasErrors() && commentInstance.save()) {
                flash.message = "Comment ${params.id} updated"
                redirect(action:list)
            }
            else {
                render(view:'edit',model:[commentInstance:commentInstance])
            }
        }
        else {
            flash.message = "Comment not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def save = {
        def commentInstance = new Comment(params)
        if(!commentInstance.hasErrors() && commentInstance.save()) {
            flash.message = "Comment ${commentInstance.id} created"
            redirect(action:list)
        }
        else {
            render(view:'create',model:[commentInstance:commentInstance])
        }
    }
}
