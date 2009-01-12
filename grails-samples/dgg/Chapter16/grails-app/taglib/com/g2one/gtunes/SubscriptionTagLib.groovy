package com.g2one.gtunes

class SubscriptionTagLib {

	def isSubscribed = { attrs, body ->
		if(checkSubscribed(request.user, attrs.artist)) {
			out << body() 
		}
	}

	def notSubscribed = { attrs, body ->
		if(!checkSubscribed(request.user, attrs.artist)) {
			out << body()  
		}		
	}
	
	boolean checkSubscribed(user, artist) {
		if(user && artist) {
			def sub = ArtistSubscription.findByUserAndArtist(user, artist, [cache:true])
			if(sub) {
				return true
			}
		}
		return false
	}
}