package com.g2one.gtunes

import grails.test.*

class StoreBuyFlowTests extends WebFlowTestCase {
	
	def controller = new StoreController()
	def getFlow() {
		controller.buyFlow
	}
	
	void setUp() {
		super.setUp()		
	}
	
	void testMissingAlbumId() {
		MockUtils.mockDomain(Album)
		startFlow()
		
		assertFlowExecutionEnded()
		assertFlowExecutionOutcomeEquals 'showStore'
	}
	
	void testNotLoggedIn() {
		MockUtils.mockDomain(Album, [new Album(title:"Aha Shack Heartbreak", id:1L)])
		controller.params.id = 1
		startFlow()
		
		assertFlowExecutionEnded()
		assertFlowExecutionOutcomeEquals 'requiresLogin'		
	}
	
	void testRequiresHardCopy() {
		MockUtils.mockDomain(Album, [new Album(title:"Aha Shake Heartbreak", id:1L)])
		def user = new User(login:"fred", id:1L)
		MockUtils.mockDomain(User, [user])
		controller.request.user = user
		controller.params.id = 1
		startFlow()
		assertCurrentStateEquals "requireHardCopy"
		
		def model = getFlowScope()
		assertEquals 1, model.albumPayments.size()
		def albumPayment = model.albumPayments[0]
		assertNotNull albumPayment?.album
		assertEquals "Aha Shake Heartbreak", albumPayment.album.title
		assertEquals albumPayment, model.lastAlbum
		
	}
}