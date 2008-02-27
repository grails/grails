package org.grails.blog

import org.grails.content.Content

class BlogEntry extends Content {

	static hasMany = [comments:Comment]
}