package com.g2one.gtunes

import grails.test.*
import com.amazonaws.a2s.*
import com.amazonaws.a2s.model.*

class AlbumArtTagLibTests extends GroovyPagesTestCase {
	
	def albumArtService

	void testNoAccessKey() {

		def template = '<music:albumArt artist="foo" album="bar" />'
		
		assertOutputEquals '<img width="200" src="'+AlbumArtService.DEFAULT_ALBUM_ART_IMAGE+'" border="0"></img>', template

	}
	
	void testAccessKeyWithNullAlbumAndArtist() {
		albumArtService.accessKeyId = "293473894732974"
		def template = '<music:albumArt  />'
		
		assertOutputEquals '', template
		
	}
	

	void testExceptionFromAmazon() {
		AmazonA2SClient.metaClass.itemSearch = { ItemSearchRequest request -> throw new Exception("test exception") }
		albumArtService.accessKeyId = "293473894732974"
		
		def template = '<music:albumArt artist="Radiohead" album="The Bends" />'
		
		assertOutputEquals '<img width="200" src="'+AlbumArtService.DEFAULT_ALBUM_ART_IMAGE+'" border="0"></img>', template

	}
	
	void testGoodResultFromAmazon() {
		AmazonA2SClient.metaClass.itemSearch = { ItemSearchRequest request -> [items:[[item:[[largeImage:[URL:"/mock/url/album.jpg"]]]]]] }

		albumArtService.accessKeyId = "293473894732974"
		
		def template = '<music:albumArt artist="Radiohead" album="Pablohoney" />'
		
		assertOutputEquals '<img width="200" src="/mock/url/album.jpg" border="0"></img>', template
	}
}