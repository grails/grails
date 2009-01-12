package com.g2one.gtunes

class SubscriptionTagLibTests extends grails.test.TagLibUnitTestCase {

	void testIsSubscribed() {
		mockDomain(ArtistSubscription)
		
		def artist = new Artist(name:"Kings of Leon")
		def user = new User(login:"testuser")
		new ArtistSubscription(artist:artist, user:user).save()
		
		tagLib.request.user = user
		tagLib.isSubscribed(artist:artist) {
			"subscribed"
		}
		tagLib.notSubscribed(artist:artist) {
			"notsubscribed"
		}		
		
		assertEquals "subscribed", tagLib.out.toString()
	}
	
	void testNotSubscribed() {
		mockDomain(ArtistSubscription)
		
		def artist = new Artist(name:"Kings of Leon")
		def user = new User(login:"testuser")
		
		tagLib.request.user = user
		tagLib.isSubscribed(artist:artist) {
			"subscribed"
		}
		tagLib.notSubscribed(artist:artist) {
			"notsubscribed"
		}
		
		
		assertEquals "notsubscribed", tagLib.out.toString()		
	}

}