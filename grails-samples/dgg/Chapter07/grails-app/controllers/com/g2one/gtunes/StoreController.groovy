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
		
		[genres:genreList.sort()]		
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
}
