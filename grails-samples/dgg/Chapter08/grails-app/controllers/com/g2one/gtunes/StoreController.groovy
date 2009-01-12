package com.g2one.gtunes

class StoreController {

    def index = {
    }

	def shop = {
		def genreList = Album.withCriteria {
			projections {
				distinct "genre"
			}
		}
		
		[
			genres:genreList.sort(),
			top5Albums:Album.list(max:5, sort:"dateCreated", order:"desc"),
			top5Songs:Song.list(max:5, sort:"dateCreated", order:"desc"),
			top5Artists:Artist.list(max:5, sort:"dateCreated", order:"desc")
		]
	}
	
	def genre = {
		def max =params.max?.toInteger() ?: 10
		def total = Album.countByGenre(params.name)
		def albumList = Album.findAllByGenre(params.name, [offset:params.offset, 
														   max:max <= 100 ? max : 10, 
														   fetch:[artist:'join']])


		return [albums:albumList.sort { it.artist.name },
			    totalAlbums:total, 
				genre:params.name]
	}
	
	
	def search = {
		def q = params.q ?: null
		def searchResults
		if(q) {
			searchResults = [
				albumResults: trySearch { Album.search(q, [max:10]) },
				artistResults: trySearch { Artist.search(q, [max:10]) },
				songResults: trySearch { Song.search(q, [max:10]) },								
				q: q.encodeAsHTML()
			]
		} 
		
		render(template:"searchResults", model: searchResults)
	}

	def trySearch(Closure callable) {
		try {
			return callable.call()
		}
		catch(Exception e) {
			log.debug "Search Error: ${e.message}", e
			return []
		}
	}
	

}
