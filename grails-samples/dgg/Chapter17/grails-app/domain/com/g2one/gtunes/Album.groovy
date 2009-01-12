package com.g2one.gtunes

class Album implements Serializable{
	
    static searchable = [only: ['genre', 'title']]

	Date dateCreated
	Date lastUpdated
    String title
	Integer year
	String genre
	MonetaryAmount price
    
	List songs
    static hasMany = [songs:Song]
    static belongsTo = [artist:Artist]

	transient jmsTemplate
	transient afterInsert = {
		try {
			jmsTemplate?.convertAndSend("artistSubscriptions", this)			
		}
		catch(Exception e) {
			log.error "Error sending JMS message: ${e.message}",e
		}
	}
	
    static constraints = {
        title(blank:false)
		year range:1900..2100
    }

	static mapping = {
        price type: MonetaryAmountUserType, {
               column name: "price"
               column name: "currency_code", sqlType: "text"
        }		
	}

	static optionals = ['genre', 'year']

	String toString() { title }
	

}
