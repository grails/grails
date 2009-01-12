package com.g2one.gtunes

class SongTests extends grails.test.GrailsUnitTestCase {

    void testMinimumDuration() {
		mockDomain(Song)
        def song = new Song(duration: 0)
        assertFalse 'validation should have failed', song.validate()
		assertEquals "min", song.errors.duration
    }
}
