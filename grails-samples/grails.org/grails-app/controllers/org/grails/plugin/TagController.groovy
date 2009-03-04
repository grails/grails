package org.grails.plugin

import grails.converters.JSON

/*
 * author: Matthew Taylor
 */
class TagController {

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

    def cloud = {
        def tagCounts = Tag.list().inject([:]) { tagCount, tag ->
            tagCount[tag.name] = tag.plugins.size()
            tagCount
        }
        render(view: '/plugin/tagCloud', model:[tagCounts:tagCounts])
    }

    def show = {
        redirect(controller:'plugin', action:'list', fragment:"${params.selectedTag} tags")
    }

}