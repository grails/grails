package com.g2one.gtunes

class AlbumPayment implements Serializable{
	Album album
	User user
	Address shippingAddress
	
	static constraints = {
		shippingAddress nullable:true
	}
}