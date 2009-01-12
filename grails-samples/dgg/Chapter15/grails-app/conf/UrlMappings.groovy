
class UrlMappings {
    static mappings = {
	  "/"(controller:"store")
	"/music/$artistName/$albumName?/$songName?"{
		controller = {
			if(params.artistName && params.albumName && params.songName) return 'song'
			else if(params.artistName && params.albumName) return 'album'
			else return 'artist'
		}
		action = [GET:'show', PUT:'save', POST:'update', DELETE:'delete']
	}
	
	  // User access
	  "/store/latest"(controller:"store",action:"latest")
	  "/your/music"(controller:"user", action:"music")
	  "/login"(controller:"user", action:"login")
	  "/logout"(controller:"user", action:"logout")	
	  "/register"(controller:"user", action:"register")	
	  "/stream/$id"(controller:"song", action:"stream")
	  "/play/$id"(controller:"song", action:"play")	
	  "/buy/$id"(controller:"store", action:"buy")		
	  "/blog"(controller:"blog", action:"list")
	
	   // anonymous browsing
	  "/album/$id"(controller:"album", action:"display")
	  "/song/$id"(controller:"song", action:"display")	
	  "/artist/$id"(controller:"artist", action:"display")
	  "/store"(controller:"store", action:"shop")
	  "/search"(controller:"store", action:"search")
	  "/genre/$name"(controller:"store", action:"genre") 	

	  // Administrator access
	  "/blog/post"(controller:"blog") {
			action = [GET:"create", POST:"save"]
	   }
      "/admin/$controller/$action?/$id?"()

	  // Errors
	  "500"(controller:'errors', action:'index')
	}
}
