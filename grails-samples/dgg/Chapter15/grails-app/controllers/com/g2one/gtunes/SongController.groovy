package com.g2one.gtunes

class SongController {
	def scaffold = Song
	
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
		def song = Song.get(params.id)
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
	
	static final BUFFER_SIZE = 2048
	def stream = {
		def song = Song.get(params.id)
		if(song) {
			def albumPermission = new AlbumPermission(album:song.album)
			jsec.hasPermission(permission:albumPermission) {
				def file = new File(song.file)				
				try {
					def type = file.name[-3..-1]
					response.contentType = "audio/x-${type}"
					def out = response.outputStream
					def bytes = new byte[BUFFER_SIZE]				
					file.withInputStream { inp ->
						while( inp.read(bytes) != -1) {
							out.write(bytes)						
							out.flush()						
						}
					}
				}
				catch(Exception e) {
					log.error "Error streaming song $file: $e.message", e
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
