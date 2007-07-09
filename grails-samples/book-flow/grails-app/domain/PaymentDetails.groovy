class PaymentDetails implements Serializable { 
	
	String cardNumber
	String expiryDate
	
	static constraints = {       
		cardNumber(creditCard:true)
		expiryDate(matches:"\\d{2}/\\d{2}")
	}
	
}