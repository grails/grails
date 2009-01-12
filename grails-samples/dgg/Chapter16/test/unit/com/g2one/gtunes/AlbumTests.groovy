package com.g2one.gtunes

class AlbumTests extends grails.test.GrailsUnitTestCase {

    void testNullTitle() {
		mockDomain(Album)
        def album = new Album()
        assertFalse 'validation should have failed', album.validate()
		assertEquals "nullable", album.errors.title
    }

    void testBlankTitle() {
		mockDomain(Album)
        def album = new Album(title:'')
        assertFalse 'validation should have failed', album.validate()
		assertEquals "blank", album.errors.title
    }
}
