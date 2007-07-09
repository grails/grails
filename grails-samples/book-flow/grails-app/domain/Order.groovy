class Order implements Serializable {    
	static belongsTo = [person:Person]      
	static hasMany = [items:Book]
	
	String invoiceNumber
	Address shippingAddress
	PaymentDetails paymentDetails        

	static embedded = ['shippingAddress', 'paymentDetails']
	
	static constraints = {
		shippingAddress(nullable:false)
		paymentDetails(nullable:false)
	}
}