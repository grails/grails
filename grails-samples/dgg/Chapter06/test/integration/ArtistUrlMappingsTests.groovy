class ArtistUrlMappingsTests extends grails.test.GrailsUrlMappingsTestCase {

    void testShowArtist() {
        assertUrlMapping([controller: 'artist', action: 'display'], '/showArtist/Jeff_Beck') { 
            artistName = 'Jeff_Beck'
        }
    }
}