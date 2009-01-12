package com.g2one.gtunes

class ArtistController {

    def display = {
        
        // TODO pending some more work... will get back to this
        def artist
        if(params.id) {
            artist = Artist.get(params.id)
        } else if(params.artistName) {
            artist = Artist.findByName(params.artistName.replaceAll('_', ' '))
        }
        if(artist) {
            def albums = Album.findAllByArtist(artist)
            render(template:"artist", model:[artist:artist, albums:albums])
        }
        else {
            render "Artist not found."
        }       
    }
}