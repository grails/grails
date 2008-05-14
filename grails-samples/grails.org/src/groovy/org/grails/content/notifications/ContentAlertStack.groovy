package org.grails.content.notifications

import org.grails.content.Content

/**
 *
 * A class that holds references to items of content that need to be delivered as change notifications
 *
 * @author Graeme Rocher
 * @since 1.0
 * 
 * Created: May 14, 2008
 */
class ContentAlertStack {

    private changedContent = []

    synchronized pushOnStack(Content content) {
        changedContent << content
    }
    
    synchronized Content popOffStack() {
        if(!changedContent) return null
        def last = changedContent[-1]
        changedContent.remove(last)
        return last
    }

}