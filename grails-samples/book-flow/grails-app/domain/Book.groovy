class Book implements Serializable {
	String title
	String author
	Double price
	
	static constraints = {
		title(blank:false)
		author(blank:false)
	}
}