class BootStrap {

     def init = { servletContext ->
		assert new Test(age:10, name:"bob")
					.addToChildren(name:"joy")
					.save(flush:true)
		
     }
     def destroy = {
     }
} 