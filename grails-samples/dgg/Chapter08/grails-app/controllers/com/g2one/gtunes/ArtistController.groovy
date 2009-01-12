package com.g2one.gtunes

class ArtistController {
	def display = {
		def artist = Artist.get(params.id)
		if(artist) {
			def albums = Album.findAllByArtist(artist)
			render(template:"artist", model:[artist:artist, albums:albums])
		}
		else {
			render "Artist not found."
		}		
	}
}