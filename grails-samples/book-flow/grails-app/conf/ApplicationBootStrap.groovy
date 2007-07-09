class ApplicationBootStrap {

     def init = { servletContext ->     
		new Book(title:"Groovy in Action", author: "Dierk Koenig", price: 35.00 as Double).save()
		new Book(title:"The Definition Guide to Grails", author:"Graeme Rocher", price: 25.00 as Double).save()
		new Book(title:"Java Persistence with Hibernate", author:"Gavin King, Christian Bauer", price:35.00 as Double).save()
	 }
	             
     def destroy = {
     }
} 