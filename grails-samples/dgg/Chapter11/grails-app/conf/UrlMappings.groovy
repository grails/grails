class UrlMappings {
    static mappings = {
	  "/"(controller:"store")
	  "/genre/$name"(controller:"store", action:"genre") 
      "/$controller/$action?/$id?"{
	      constraints {
			 // apply constraints here
		  }
	  }
	  "500"(controller:'errors', action:'index')
	}
}
