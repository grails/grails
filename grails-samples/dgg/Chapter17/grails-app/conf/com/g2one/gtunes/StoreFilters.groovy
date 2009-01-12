package com.g2one.gtunes

class StoreFilters {
	
	static filters = {
		top5(controller:"(album|store|artist)", action:"*") {
			after = { model ->
				if(model) {
					model.top5Albums = Album.list(max:5, sort:"dateCreated", order:"desc")
					model.top5Songs = Song.list(max:5, sort:"dateCreated", order:"desc")
					model.top5Artists = Artist.list(max:5, sort:"dateCreated", order:"desc")									
				}
			}
		}
	}
}