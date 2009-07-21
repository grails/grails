class Book {
    String title
    Author author
    
    static constraints = {
        author(nullable:true)
    }
}
