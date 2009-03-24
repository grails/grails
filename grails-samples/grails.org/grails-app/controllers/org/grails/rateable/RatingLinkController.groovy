

package org.grails.rateable

import org.grails.plugin.Plugin

class RatingLinkController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        def list = RatingLink.list(params)
        list.each {
            // only ratings on plugins at the moment...
            def plugin = Plugin.get(it.ratingRef)
            it.metaClass.getPlugin = { -> plugin }
        }
        [ ratingLinks: list, total: RatingLink.count() ]
    }

    def delete = {
        def ratingLinkInstance = RatingLink.get( params.id )
        if(ratingLinkInstance) {
            try {
                ratingLinkInstance.delete()
                flash.message = "RatingLink ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "RatingLink ${params.id} could not be deleted"
                redirect(action:list)
            }
        }
        else {
            flash.message = "RatingLink not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def ratingInstance = Rating.get( params.id )

        if(!ratingInstance) {
            flash.message = "Rating not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ ratingInstance : ratingInstance ]
        }
    }

    def update = {
        def ratingInstance = Rating.get( params.id )
        if(ratingInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(ratingInstance.version > version) {

                    ratingInstance.errors.rejectValue("version", "rating.optimistic.locking.failure", "Another user has updated this Rating while you were editing.")
                    render(view:'edit',model:[ratingInstance:ratingInstance])
                    return
                }
            }
            ratingInstance.properties = params
            if(!ratingInstance.hasErrors() && ratingInstance.save()) {
                flash.message = "Rating ${params.id} updated"
                redirect(action:list)
            }
            else {
                render(view:'edit',model:[ratingInstance:ratingInstance])
            }
        }
        else {
            flash.message = "Rating not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def save = {
        def ratingInstance = new Rating(params)
        if(!ratingInstance.hasErrors() && ratingInstance.save()) {
            flash.message = "Rating ${ratingInstance.id} created"
            redirect(action:list)
        }
        else {
            render(view:'create',model:[ratingInstance:ratingInstance])
        }
    }
}
