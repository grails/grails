package com.g2one.gtunes

class AlbumController {

	def display = {
		def album = Album.findById(params.id, [fetch:[artist:'join']])
		if(album) {
			def artist = album.artist 
			println "ALBUM IS $album"
			println "ARTIST IS $artist"
			if(request.xhr) {
				render(template:"album", model:[artist:artist, album:album])				
			}
			else {
				render(view:"show", model:[artist:artist, album:album])
			}
		}
		else {
			response.sendError 404
		}
	}
	
	def show = {
		def a = Album.get(params.id)
		if(a) {
			return [album:a, artist:a.artist]
		}
		else {
			response.sendError 404
		}
	}
}
