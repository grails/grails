class Address implements Serializable {
	String number
	String postCode    
	
	static constraints = {
		number(blank:false)
		postCode(minSize:6)
	}
}