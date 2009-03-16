package com.g2one.gtunes

import com.amazonaws.a2s.*
import com.amazonaws.a2s.model.*
import grails.test.GrailsUnitTestCase

class AlbumArtServiceTests extends GrailsUnitTestCase {
	
	void tearDown() {
		GroovySystem.metaClassRegistry.removeMetaClass(AmazonA2SClient)
	}
	
	void setUp() {
	    super.setUp()
	    mockLogging(AlbumArtService)
	}
	
	void testNoAccessKey() {
		def albumArtService = new AlbumArtService()
		
		
		assertEquals AlbumArtService.DEFAULT_ALBUM_ART_IMAGE, albumArtService.getAlbumArt("foo", "bar")
	}
	
	void testAccessKeyWithNullAlbumAndArtist() {
		def albumArtService = new AlbumArtService()
		albumArtService.accessKeyId = "293473894732974"
		
		assertEquals AlbumArtService.DEFAULT_ALBUM_ART_IMAGE, albumArtService.getAlbumArt(null, null)
		
	}
	

	void testExceptionFromAmazon() {
		AmazonA2SClient.metaClass.itemSearch = { ItemSearchRequest request -> throw new Exception("test exception") }
		def albumArtService = new AlbumArtService()
		albumArtService.cacheService = [cacheOrReturn:{key, callable-> callable() }]
		albumArtService.accessKeyId = "293473894732974"
		
		assertEquals AlbumArtService.DEFAULT_ALBUM_ART_IMAGE, albumArtService.getAlbumArt("Radiohead", "The Bends")		
	}
	
	void testGoodResultFromAmazon() {
		AmazonA2SClient.metaClass.itemSearch = { ItemSearchRequest request -> [items:[[item:[[largeImage:[URL:"/mock/url/album.jpg"]]]]]] }
		def albumArtService = new AlbumArtService()
		albumArtService.cacheService = [cacheOrReturn:{key, callable-> callable() }]		
		albumArtService.accessKeyId = "293473894732974"
		
		assertEquals "/mock/url/album.jpg", albumArtService.getAlbumArt("Radiohead", "The Bends")			
	}
}