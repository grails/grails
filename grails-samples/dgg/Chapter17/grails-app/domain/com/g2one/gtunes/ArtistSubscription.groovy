package com.g2one.gtunes

class ArtistSubscription extends Subscription {
	
	ArtistSubscription() {
		type = SubscriptionType.ARTIST
	}
	
	static belongsTo = [artist:Artist]
}