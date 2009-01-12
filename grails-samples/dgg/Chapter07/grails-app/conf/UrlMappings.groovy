class UrlMappings {
    static mappings = {
	  "/"(controller:"store")
	  "/genre/$name"(controller:"store", action:"genre")
      "/$controller/$action?/$id?"{
	      constraints {
			 // apply constraints here
		  }
	  }
	  "/store/$lang"(controller:'store')
	  
	  "500"(view:'/error')
	}
}
