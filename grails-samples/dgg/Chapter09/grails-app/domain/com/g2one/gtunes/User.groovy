package com.g2one.gtunes

class User implements Serializable{
	
	Date dateCreated
	Date lastUpdated
	
	String login
	String password
	String firstName
	String lastName
	static hasMany = [purchasedAlbums:Album, purchasedSongs:Song]
	
	static constraints = {
		login blank:false, size:5..15,matches:/[\S]+/
		password blank:false, size:5..15,matches:/[\S]+/
		firstName blank:false
		lastName blank:false
	}
}