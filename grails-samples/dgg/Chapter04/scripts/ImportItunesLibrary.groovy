import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU
import org.xml.sax.*
import javax.xml.parsers.SAXParserFactory

grailsHome = Ant.project.properties."environment.GRAILS_HOME"

includeTargets << new File ( "${grailsHome}/scripts/Bootstrap.groovy" )  

target('default': "Imports an existing iTunes library into the gTunes application") {
    packageApp()
    classLoader = new URLClassLoader([classesDir.toURL()] as URL[], rootLoader)
    Thread.currentThread().setContextClassLoader(classLoader)    
    loadApp()
    configureApp()
	
    importItunesLibrary()
}

target(importItunesLibrary: "The implementation task") {
	if(args) {
        def userClass = grailsApp.getDomainClass("com.g2one.gtunes.Album").clazz
        def file = new File(args.replaceAll(/\n/, ' ').trim())
		if(file.exists()) {
			println "Loading XML, this can take a while..."
			def parser = SAXParserFactory.newInstance().newSAXParser()
			def handler = new SAXItunesParser()
			handler.grailsApp = grailsApp
			parser.parse(file, handler)
			println "Finished reading XML, importing to app.."
			def artists = handler.artists.values().findAll { it.albums?.artist }
			for(a in artists) {
				try {
					a.save(flush:true)
				}
				catch(Exception e) {
					println "Error saving artist ${a.name}"
				}
			}

		}
		else {
			println "File ${args.replaceAll(/\n*/, /\s/).trim()} does not exist"
		}
	}
	else {
		println "Please specify confluence XML file"
	}
}
class SAXItunesParser extends  org.xml.sax.helpers.DefaultHandler {
	static final ITUNES_KEY_MAP = [
		Name:'title',
		'Total Time':'duration',
		'Album':'album',
		'Artist':'artist',
		'Track Number':'trackNumber',
		'Genre': 'genre',
		'Year':'year'
	]
	def grailsApp
	def songs = [:]
	def lastSong = null
	def albums = [:]
	def artists = [:]
	def newSong = false
	def songProps = false
	def propName = false
	def propValue = true
	def lastPropName = null
	def lastSongProps = [:]
	def lastAlbum
	def lastArtist
	def newSongId = false
	def currentBuffer = new StringBuffer()
	
	void startElement(String uri, String localName, String qName, Attributes attributes) {
		if(newSong && qName == 'integer') {
			newSongId = true
			newSong = false
		}
		if(songProps && qName == 'key') {
			propName = true
		}
		else if(songProps && qName != 'key') {
			propValue = true
		}
	}
	
	void endElement(String namespaceURI, String localName, String qName)  {
		if(newSongId) {
			lastSong = currentBuffer.toString()

			currentBuffer.delete(0, currentBuffer.size())
			songs[lastSong] = grailsApp.getDomainClass("com.g2one.gtunes.Song").newInstance()
			lastSongProps[lastSong] = [:]
			newSongId = false
			songProps = true			
		}
		else if(songProps && propName) {
			lastPropName = currentBuffer.toString()
			currentBuffer.delete(0, currentBuffer.size())
			propName = false
		}
		else if(songProps && qName == 'dict') {
			songProps = false
			def props = lastSongProps[lastSong]
			def song = songs[lastSong]

			song.properties = props
			if(lastArtist && lastAlbum) {
				song.artist = lastArtist
				song.album = lastAlbum
				lastAlbum.genre = song.genre
				lastAlbum.year = song.year				
				lastArtist.addToAlbums(lastAlbum)
				lastArtist = null
				lastAlbum = null
			}
			else {
				songs.remove(lastSong)
			}
		}
		else if(songProps && qName != 'key') {
			propValue = false
			def prop = ITUNES_KEY_MAP[lastPropName]
			def value = currentBuffer.toString()
			currentBuffer.delete(0, currentBuffer.size())

			lastSongProps[lastSong]."${prop}" = value							
			if(prop == 'album') {
				def album = albums[value]
				if(!album) {
					album = grailsApp.getDomainClass("com.g2one.gtunes.Album").newInstance()
					album.title = value
					albums[value] = album					
				}
				album.addToSongs(songs[lastSong])
				lastAlbum = album				
			}
			if(prop == 'artist') {
				def artist = artists[value]
				if(!artist) {
					artist = grailsApp.getDomainClass("com.g2one.gtunes.Artist").newInstance()
					artist.name = value
					artists[value] = artist
				}
				lastArtist = artist
			}			
		}
		
	}
	
	void ignorableWhitespace(char[] ch,
	                                int start,
	                                int length) {
		
	}
	
	
	void characters(char[] ch,
	                       int start,
	                       int length) {

		def value = String.copyValueOf(ch, start, length)


		if(value == 'Track ID') {
			newSong = true
		}
		else if(newSongId) {
			currentBuffer << value			
		}
		else if(songProps && propName) {
			currentBuffer << value
		}
		else if(songProps && propValue) {
			currentBuffer << value
		}
	}
}