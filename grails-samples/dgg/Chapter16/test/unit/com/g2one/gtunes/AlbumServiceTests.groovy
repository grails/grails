package com.g2one.gtunes

import grails.test.*

class AlbumServiceTests extends GroovyTestCase {

    void testFindAlbumsForArtist() {
		
		def artist = new Artist(name:"Kings of Leon")
		MockUtils.mockDomain(Artist, [artist])
		MockUtils.mockDomain(Album, [new Album(title:"Because of the times", artist:artist),
									 new Album(title:"Aha Shake Heartbreak", artist:artist)])
									
									
		def albumService = new AlbumService()
		
		def results =  albumService.findAlbumsForArtist("Kings of Leon")
		assertEquals 2,results.size()
		assertEquals "Because of the times", results[0]
		
		results =  albumService.findAlbumsForArtist("Rubbish")
		
		assertEquals 0, results.size()
    }
}
