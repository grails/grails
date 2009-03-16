

package org.grails.taggable

import grails.converters.JSON

class TagController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ tagInstanceList: Tag.list( params ), tagInstanceTotal: Tag.count() ]
    }

    def show = {
        def tagInstance = Tag.get( params.id )

        if(!tagInstance) {
            flash.message = "Tag not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ tagInstance : tagInstance ] }
    }

    def delete = {
        def tagInstance = Tag.get( params.id )
        if(tagInstance) {
            try {
                tagInstance.delete()
                flash.message = "Tag ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Tag ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Tag not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def tagInstance = Tag.get( params.id )

        if(!tagInstance) {
            flash.message = "Tag not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ tagInstance : tagInstance ]
        }
    }

    def update = {
        def tagInstance = Tag.get( params.id )
        if(tagInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(tagInstance.version > version) {
                    
                    tagInstance.errors.rejectValue("version", "tag.optimistic.locking.failure", "Another user has updated this Tag while you were editing.")
                    render(view:'edit',model:[tagInstance:tagInstance])
                    return
                }
            }
            tagInstance.properties = params
            if(!tagInstance.hasErrors() && tagInstance.save()) {
                flash.message = "Tag ${params.id} updated"
                redirect(action:show,id:tagInstance.id)
            }
            else {
                render(view:'edit',model:[tagInstance:tagInstance])
            }
        }
        else {
            flash.message = "Tag not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def tagInstance = new Tag()
        tagInstance.properties = params
        return ['tagInstance':tagInstance]
    }

    def save = {
        def tagInstance = new Tag(params)
        if(!tagInstance.hasErrors() && tagInstance.save()) {
            flash.message = "Tag ${tagInstance.id} created"
            redirect(action:show,id:tagInstance.id)
        }
        else {
            render(view:'create',model:[tagInstance:tagInstance])
        }
    }

    def autoCompleteNames = {
        def delimits
        def csvPre = ''
        def query = params.query
        if (query.contains(',') && !query.trim().endsWith(',')) {
            delimits = query.trim().split(',')
            csvPre = delimits[0..-2].join(', ') + ', '
            query = delimits[-1]
        }
        def tags = Tag.findAllByNameLike("${query.trim()}%")
        response.setHeader("Cache-Control", "no-store")

        def results = tags.collect {
            [ id: it.id, name: csvPre + it.name ]
        }

        def data = [ tagResults: results ]
        render data as JSON
    }
}
