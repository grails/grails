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
	
	void testDontRequireHardCopyWithNoRecommendations() {
		MockUtils.mockDomain(Album, [new Album(title:"Aha Shake Heartbreak", id:1L)])
		Album.metaClass.static.withCriteria = { Closure s -> []}
		MockUtils.mockDomain(AlbumPayment)
		AlbumPayment.metaClass.static.withCriteria = { Closure s -> []}
				
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
		
		signalEvent "no"
		
		assertCurrentStateEquals "enterCardDetails"		
		
		assert !model.shippingAddress
		assert !model.genreRecommendations
	}
	
	
	void testEnterShippingAddress() {
		currentState = "requireHardCopy"
		signalEvent "yes"
		assertCurrentStateEquals "enterShipping"
		signalEvent "back"		
		assertCurrentStateEquals "requireHardCopy"		
		signalEvent "yes"
		assertCurrentStateEquals "enterShipping"
		signalEvent "next"		
		assertCurrentStateEquals "enterShipping"
		def model = getFlowScope()
		
		def errors = model.shippingAddress?.hasErrors()
		assertNotNull errors
		assertTrue errors
		
		model.lastAlbum = new AlbumPayment(album:new Album(title:"Aha Shack Heartbreak"))
		model.albumPayments = [model.lastAlbum]
		controller.params.number = "10"		
		controller.params.street = "John Doe Street"
		controller.params.city = "London"
		controller.params.state = "Greater London"
		controller.params.postCode = "W134G"
		controller.params.country = "United Kingdom"
		assertNotNull model.shippingAddress
		signalEvent "next"
		assertCurrentStateEquals "enterCardDetails"		
		def shippingAddress = model.shippingAddress
		
		assertNotNull shippingAddress
		assertTrue shippingAddress.validate()
	}
	
	void testEnterCreditCardDetails() {
		currentState = "enterCardDetails"
		signalEvent "next"
		
		assertCurrentStateEquals "enterCardDetails"
		
		controller.params.clear()
		controller.params.name = "MR John Doe"
		controller.params.number = "648905840533"
		controller.params.expiry = "rubbish"
		controller.params.code  = "toolongg"
		
		signalEvent "next"
		assertCurrentStateEquals "enterCardDetails"		
				
		controller.params.clear()
		controller.params.name = "MR John Doe"
		controller.params.number = "4111111111111111"
		controller.params.expiry = "01/01"
		controller.params.code  = "999"
		
		signalEvent "next"
		assertCurrentStateEquals "showConfirmation"	
	}
	
	void testConfirmPurchase() {
		currentState = "showConfirmation"
		def model = getFlowScope()
		model.creditCard = new CreditCardCommand(
							name :"MR Joe Bloggs",
							number:"4111111111111111",
							expiry:"01/01",
							code  :999			
							)

		model.user = new User(login:"joebloggs", password:"foobar", firstName:"Joe", lastName:"Bloggs")
		model.user.id = 1L

		model.albumPayments = [ new AlbumPayment(album:new Album(title:"Aha Shake Heartbreak"))]
		MockUtils.mockDomain(AlbumPayment, model.albumPayments)		
		Payment.metaClass.save = { Map m -> delegate }
		User.metaClass.save = { Map m -> delegate }
		
		
		signalEvent "confirm"
		
		assertFlowExecutionEnded()
		assertFlowExecutionOutcomeEquals 'displayInvoice'		
		
	}
}
