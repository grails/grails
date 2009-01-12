package com.g2one.gtunes

class AlbumController {
	def index = {}
	
	def show = {
		def a = Album.get(params.id)
		if(a) {
			return [album:a]
		}
		else {
			response.sendError 404
		}
	}
}
