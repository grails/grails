package com.g2one.gtunes

class User {
	
	Date dateCreated
	Date lastUpdated
	
	String login
	String password
	String firstName
	String lastName
	static hasMany = [purchasedSongs:Song]
	
	static constraints = {
		login blank:false, size:5..15,matches:/[\S]+/
		password blank:false, size:5..15,matches:/[\S]+/
		firstName blank:false
		lastName blank:false
	}
}