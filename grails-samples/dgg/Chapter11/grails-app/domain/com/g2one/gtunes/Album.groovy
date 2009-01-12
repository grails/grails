package com.g2one.gtunes

class Album implements Serializable{
	
    static searchable = [only: ['genre', 'title']]

	Date dateCreated
	Date lastUpdated
    String title
	Integer year
	String genre
	Float price = 0.0
    
	List songs
    static hasMany = [songs:Song]
    static belongsTo = [artist:Artist]

    static constraints = {
        title(blank:false)
		year range:1900..2100
		price scale:2
    }

	static optionals = ['genre', 'year']

	String toString() { title }
	

}
