package com.g2one.gtunes

class SongController {
	def scaffold = Song
	
	def display = {
		def song = Song.get(params.id)
		if(song)
			redirect(controller:"album", action:"display", id:song.album.id)
	}
}
