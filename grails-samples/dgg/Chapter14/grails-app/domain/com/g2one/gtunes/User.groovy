package com.g2one.gtunes

class User implements Serializable{
	
	Date dateCreated
	Date lastUpdated

	String login
	String password
	String firstName
	String lastName
	String email
	static hasMany = [  purchasedAlbums:Album, 
						purchasedSongs:Song, 
						roles:Role, 
						permissions:Permission]
	
	static constraints = {
		login blank:false, nullable:false,size:5..15,matches:/[\S]+/, unique:true
		password blank:false,nullable:false,matches:/[\S]+/
		firstName blank:false,nullable:false
		lastName blank:false,nullable:false
		email email:true, blank:false, nullable:false,unique:true
		
		roles fetch:"join"
		permissions fetch:"join"
	}
}