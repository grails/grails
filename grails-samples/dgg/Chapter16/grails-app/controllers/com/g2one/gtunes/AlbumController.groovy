package com.g2one.gtunes

import grails.converters.*

class AlbumController {
	
	def scaffold = Album
	
	def show = {
		def album 
		def artist
		// TODO: replace for multiple assignment when Groovy 1.6 is merged
		def list = getAlbumAndArtist(params)
		album = list[0]
		artist = list[1]
		
		if(album) {
			return withFormat {
				html artist:artist, album:album
				js { render(template:'album', model:[artist:artist, album:album]) } 
				xml { render album as XML }
				json { render album as JSON }
			}			
		}
		else {
			response.sendError 404
		}
	}
	
	def getAlbumAndArtist(params) {
		def album 
		def artist
		if(params.artistName && params.albumName) {
			artist = Artist.findByName(params.artistName)
			if(artist) {
				album = Album.findByArtistAndTitle(artist, params.albumName)
			}
		}
		else {
			album = Album.get(params.id)
			artist = album?.artist
		}
		return [album, artist]		
	}
	
	def update = {
		Album.withTransaction { status ->
			def album = Album.get(params['album']?.id) 
			if(album) { 
				album.properties = params['album']
				
				if(!album.save(flush:true)) {
					status.setRollbackOnly()
				}
				return withFormat {
					html {
						render(view:"show", [album:album, artist:album.artist])											
					}
					xml {
						if(album.validate()) {
							render album as XML
						}
						else {
							render(contentType:"text/xml",
								   text:g.renderErrors(bean:album, as:"xml"))
						}
					}					
				}
				
			}
			else {
				response.sendError 404
			}			
		}
	}

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
	
}
