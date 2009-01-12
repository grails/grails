package com.g2one.gtunes

class AlbumController {

	def scaffold = Album
	
	def display = {
		def album = Album.get(params.id)
		if(album) {
			def artist = album.artist 
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
