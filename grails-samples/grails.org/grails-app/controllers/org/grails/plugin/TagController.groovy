package org.grails.plugin

import grails.converters.JSON

/*
 * author: Matthew Taylor
 */
class TagController {

    def autoCompleteNames = {
        println "autoCompleteNames: $params"
        def tags = Tag.findAllByNameLike("${params.query}%")
        response.setHeader("Cache-Control", "no-store")

        def results = tags.collect {
            [ id: it.id, name: it.name ]
        }

        def data = [ tagResults: results ]
        render data as JSON
    }

}