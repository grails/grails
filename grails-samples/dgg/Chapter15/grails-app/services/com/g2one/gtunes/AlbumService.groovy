package com.g2one.gtunes

class AlbumService {

	static expose = ['xfire']

    String[] findAlbumsForArtist(String artistName) {
		def artist = Artist.findByName(artistName)
		def albums = []
		if(artist) {
			albums = Album.findAllByArtist(artist)
		}
		return albums.title as String[]
    }
}
