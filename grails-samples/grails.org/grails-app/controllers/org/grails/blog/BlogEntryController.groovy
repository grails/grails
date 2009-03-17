

package org.grails.blog

class BlogEntryController {
    
    def index = { redirect(action:adminList,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def adminList = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ blogEntryInstanceList: BlogEntry.list( params ), blogEntryInstanceTotal: BlogEntry.count() ]
    }

    def show = {
        def blogEntryInstance = BlogEntry.get( params.id )

        if(!blogEntryInstance) {
            flash.message = "BlogEntry not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ blogEntryInstance : blogEntryInstance ] }
    }

    def delete = {
        def blogEntryInstance = BlogEntry.get( params.id )
        if(blogEntryInstance) {
            try {
                blogEntryInstance.delete()
                flash.message = "BlogEntry ${params.id} deleted"
                redirect(action:adminList)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "BlogEntry ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "BlogEntry not found with id ${params.id}"
            redirect(action:adminList)
        }
    }


}
