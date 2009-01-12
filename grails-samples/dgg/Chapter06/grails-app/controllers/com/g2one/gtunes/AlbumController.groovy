package com.g2one.gtunes

class AlbumController {

	def display = {
		def album = Album.get(params.id)
		if(album) {
			def artist = album.artist 
			render(template:"album", model:[artist:artist, album:album])
		}
		else {
			render "Album not found."
		}
	}
}
