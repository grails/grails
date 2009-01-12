package com.g2one.gtunes

class Address implements Serializable{ 
	String number
	String street
	String city
	String state
	String postCode
	String country

	static optionals = ['state']
	static constraints = {
		number blank:false, maxSize:200
		street blank:false, maxSize:250
		city blank:false, maxSize:200
		postCode blank:false, maxSize:50
		country blank:false, maxSize:200
	}
}