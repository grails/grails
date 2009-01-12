package com.g2one.gtunes

import grails.converters.deep.*

class ArtistController {
	def show = {
		def a = params.artistName ? Artist.findByName(params.artistName) :
		 							 Artist.get(params.id)
		
		if(a) {
			return withFormat {
				html artist:a, albums:a?.albums
				xml { render a as XML }
				json { render(contentType:"text/json") {
					name a.name
					albums {
						for(alb in a.albums) {
							album name:alb.title
						}
					}					
				}}
			}
		}
		else {
			response.sendError 404
		}
	}
	
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