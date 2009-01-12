class UrlMappings {
    static mappings = {
	  "/"(controller:"store")
      "/$controller/$action?/$id?"{
	      constraints {
			 // apply constraints here
		  }
	  }
	  "500"(view:'/error')
	}
}
