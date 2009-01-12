grailsHome = ant.project.properties."environment.GRAILS_HOME"

includeTargets << new File ( "${grailsHome}/scripts/Bootstrap.groovy" )  

target(main: "Imports data kept in the gtunes XML format into the database") {
	depends(parseArguments)
	bootstrap()
	
	def load = grailsApp.classLoader.&loadClass
	def Artist = load("com.g2one.gtunes.Artist")	
	def Album = load("com.g2one.gtunes.Album")	
	def Song = load("com.g2one.gtunes.Song")		
	def file = argsMap.params ? new File(argsMap.params[0]) : null
	
	if(!file || !file.exists()) {
		println "Specified file does not exist."
		exit(1)
	}
	else {
		def xml = new XmlSlurper().parse(file)
		println "Importing. Please wait..."
		Artist.withTransaction { status ->
			Artist.withSession { session ->
				for(a in xml.artist) {
					def artist = Artist.newInstance(name:a.@name.text())
					println "Importing artist $artist.name.."
					for(alb in a.album) {
						def album = Album.newInstance(title:alb.@title.text(),
														   genre:alb.@genre.text(),
														   year:alb.@year.text(),
														   price:alb.@price.text())
						artist.addToAlbums(album)
						alb.song.eachWithIndex { s, i ->

							def song = Song.newInstance(title:s.@title.text(),
															 duration:s.@duration.text(),
															 file:s.@file.text(),
															 trackNumber:i+1,
															 genre: album.genre,
															 year: album.year,
															 artist:artist)
							album.addToSongs(song)
						}
					}
				   if(!artist.save(flush:true)) {
					   status.setRollbackOnly()
					   println "Import Failed: Validation error occured import artist ${artist.name}, data appears corrupt."
					   break
				   }		
   				   session.clear()						
				}
			}	
			
			if(!status.isRollbackOnly()) {
				println "Import Successful."
			}
		}
	}
								
	
	
}
setDefaultTarget(main)
