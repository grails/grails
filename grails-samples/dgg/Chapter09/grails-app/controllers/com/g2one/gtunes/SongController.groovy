package com.g2one.gtunes

class SongController {
	def scaffold = Song
	
	def display = {
		def song = Song.get(params.id)
		if(song) {
			render(template:"/album/album", model:[album:song.album, artist:song.artist])
		}
		else {
			response.sendError 404
		}
	}
}
