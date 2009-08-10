class UrlMappings {
    static mappings = {
      "/$controller/$action?/$id?"{
	      constraints {
			 // apply constraints here
		  }
	  }
      "/"(view:"/index")

	  "/hello"(uri:"/hello.dispatch")
	  "500"(view:'/error')
	}
}
