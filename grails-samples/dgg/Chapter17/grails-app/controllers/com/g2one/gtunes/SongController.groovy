package com.g2one.gtunes

class SongController {
	def scaffold = Song
	
	StreamingService streamingService
	
	def display = {
		def song = Song.get(params.id)
		if(!request.xhr) {
			if(song)
				redirect(controller:"album", action:"display", id:song.album.id)
			else
				response.sendError 404
		}
		else {
			if(song) {
				render(template:"/album/album", model:[album:song.album, artist:song.album.artist])
			}
			else {
				response.sendError 404				
			}
		}
	}
	
	def play = {		
		def song = Song.read(params.id)
		if(song) {
			def albumPermission = new AlbumPermission(album:song.album)
			jsec.hasPermission(permission:albumPermission) {
				render(view:"play", model:[song:song])
			}
			jsec.lacksPermission(permission:albumPermission) {
				response.sendError 401
			}
		}
		else {
			response.sendError 404
		}
	}
	

	def stream = {
		def song = Song.read(params.id)
		if(song) {
			def albumPermission = new AlbumPermission(album:song.album)
			jsec.hasPermission(permission:albumPermission) {
				try {
					response.contentType = "audio/x-${song.file[-3..-1]}"
					streamingService.streamSong(song, response.outputStream)	
				}
				catch(Exception e) {
					log.error "Error streaming song $song: $e.message", e
					response.sendError 500
				}
												
			}
			jsec.lacksPermission(permission:albumPermission) {
				response.sendError 401
			}
		}	
		else {
			response.sendError 404
		}	
	}
}
