package com.g2one.gtunes

class Payment  implements Serializable {
	String invoiceNumber
	User user
	static hasMany = [albumPayments:AlbumPayment]
	
	static constraints = {
		invoiceNumber blank:false, matches:/INV-\d+?-\d+/
	}
}