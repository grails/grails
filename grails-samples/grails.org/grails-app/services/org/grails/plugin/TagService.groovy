package org.grails.plugin
/*
 * author: Matthew Taylor
 */
class TagService {

    def removeEmptyTags() {
        def emptyTags = []
        Tag.list().each { tag ->
            def pluginsForTag = Plugin.withCriteria {
                tags {
                    eq('name', tag.name)
                }
            }
            if (!pluginsForTag || !pluginsForTag.size()) {
                emptyTags << tag
            }
        }
        emptyTags.each {
            log.info "Removing empty tag '${it}'."
            it.delete()
        }
    }
}