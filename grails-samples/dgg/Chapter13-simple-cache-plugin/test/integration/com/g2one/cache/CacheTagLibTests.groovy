package com.g2one.cache

import grails.test.*

class CacheTagLibTests extends GroovyPagesTestCase {
	
	static transactional = false
	
	void testCacheTextTag() {
		def template = '<cache:text key="foo">1</cache:text><cache:text key="foo">2</cache:text><cache:text>3</cache:text><cache:text key="bar">4</cache:text>'
		
		assertOutputEquals "1134", template
	}
}