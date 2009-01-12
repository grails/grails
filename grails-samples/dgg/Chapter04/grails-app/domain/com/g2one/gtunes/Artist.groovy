package com.g2one.gtunes

class Artist implements Serializable{
	Date dateCreated
	Date lastUpdated

    static searchable = [only: ['name']]
	
    String name
    static hasMany = [albums:Album]

	String toString() { name }
	    
}
