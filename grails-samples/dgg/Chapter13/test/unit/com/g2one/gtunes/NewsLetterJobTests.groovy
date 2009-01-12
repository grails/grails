package com.g2one.gtunes

import grails.test.*

class NewsLetterJobTests extends GrailsUnitTestCase {


    void testExecute() {
		mockFor(Subscription).demand.static.withCriteria { Closure c ->
			['john@g2one.com', 'fred@g2one.com']
		}
		
		def a1 = new Album(title:"The Back Room", dateCreated:new Date()-10)
		def a2 = new Album(title:"An End Has a Start", dateCreated:new Date()-5)		
		def a3 = new Album(title:"Only By the Night", dateCreated:new Date()-3)				
		mockDomain(Album,[a1,a2,a3])
		
		def mailService = [sendMail: { callable ->
				callable.delegate = this
				callable.call()
			}
		]
		
		new NewsLetterJob(mailService:mailService).execute()

		assertEquals 2, emails.size()
		assertEquals "john@g2one.com", emails[0]
		assertEquals "fred@g2one.com", emails[1]		
		assertEquals 2, bodies.size()
		assertEquals 2, bodies[0].model.latestAlbums.size()
    }

	def emails = []
	def bodies = []
	def methodMissing(String name, args) {
		if(name == 'to') {
			emails << args[0] 
		}
		else if(name == 'body') {
			bodies << args[0]
		}
	}
}

