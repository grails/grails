class UrlMappings {
    static mappings = {
      "/$controller/$action?/$id?"{
	      constraints {
			 // apply constraints here
		  }
	  }
	  "/path/$file.$ext"(controller:"test", action:"testExtension")
      "/"(view:"/index")
	  "500"(view:'/error')
	}
}
