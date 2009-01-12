package com.g2one.gtunes

class Album implements Serializable{
	

	Date dateCreated
	Date lastUpdated
    String title
	Integer year
	String genre
    
	List songs
    static hasMany = [songs:Song]
    static belongsTo = [artist:Artist]

    static constraints = {
        title(blank:false)
		year range:1900..2100
    }

	static optionals = ['genre', 'year']

	String toString() { title }
	

}
