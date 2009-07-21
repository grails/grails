class BookIntegrationTests extends GroovyTestCase {
    void testSettingAuthorToNull() {
        def author1 = new Author(name:'Graeme Rocher')
        assert author1.save()
        
        def book1 = new Book(title:'Def Guide To Grails')
        assert book1.save()
        
        book1.author = author1
        book1.save(flush:true)
        println "Book before update is: ${book1.dump()}"
        
        def controller = new BookController()
        
        controller.params.id = book1.id.toString()
        controller.params['author.id'] = ''
        controller.params.title = book1.title
        controller.update()
        
        assertNotNull controller.response.redirectedUrl
        assertTrue controller.response.redirectedUrl.contains("show")

        // Now check the state of the data
        println "Flash scope: ${controller.flash}"
        println "Redir url: ${controller.response.redirectedUrl}"
        
        book1.get( book1.id)
        println "Book is: ${book1.dump()}"
        assertNull book1.author
    }
    
}