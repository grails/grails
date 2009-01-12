package com.g2one.gtunes

class Subscription {
	SubscriptionType type
	User user
	
	static mapping = {
		user fetch:"join"
	}
}
enum SubscriptionType {
	NEWSLETTER, ARTIST
}