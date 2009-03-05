package org.grails.taggable

/*
 * author: Matthew Taylor
 */
class TagService {

    def removeEmptyTags() {
        def emptyTags = []
        Tag.list().each { tag ->
            if (!TagLink.findByTag(tag)) {
                emptyTags << tag
            }
        }
        emptyTags.each {
            log.info "Removing empty tag '${it}'."
            it.delete()
        }
    }
}