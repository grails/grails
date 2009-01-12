package com.g2one.gtunes

class StoreController {

    def index = {
    }

	def shop = {
		[
			albums:Album.list(max:5, sort:"dateCreated", order:"desc"),
			songs:Song.list(max:5, sort:"dateCreated", order:"desc"),
			artists:Artist.list(max:5, sort:"dateCreated", order:"desc")
		]
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
