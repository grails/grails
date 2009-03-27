class UrlMappings {
    static mappings = {
      "/$controller/$action?/$id?"{
	      constraints {
			 // apply constraints here
		  }
	  }
      "/"(view:"/index")
	  "500"(view:'/error')
	  "500"(controller:"errors", action:"customException", exception:IllegalStateException)	
	  "500"(controller:"errors", action:"displayLayout", exception:IllegalArgumentException)
	  "404"(controller:"errors", action:"redirectAction")
	}
}
