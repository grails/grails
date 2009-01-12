grailsHome = ant.project.properties."environment.GRAILS_HOME"

includeTargets << new File ( "${grailsHome}/scripts/Bootstrap.groovy" )  

target(main: "Exports the gTunes library contained within the database into an XML file") {
	depends(parseArguments, bootstrap)

	def file = argsMap.params ? new File(argsMap.params[0]) : 
								new File("./gtunes-data-${System.currentTimeMillis()}.xml")

	def Artist = grailsApp.classLoader.loadClass("com.g2one.gtunes.Artist")
	def artistCount = Artist.count()
		
	println "Creating XML for $artistCount artists"
	new FileWriter(file) << new groovy.xml.StreamingMarkupBuilder().bind {
		music {
			Artist.withTransaction {
				Artist.withSession { session ->
					0.step(artistCount, 10) { offset ->
						def artistList = Artist.list(offset:offset, max:10, fetch:[albums:'join'])
						for(currentArtist in artistList) {
							artist(name:currentArtist.name) {
								for(currentAlbum in currentArtist.albums) {
									album(currentAlbum.properties['title', 'year', 'genre', 'price']) {
										for(currentSong in currentAlbum.songs) {
											song(currentSong.properties['title', 'duration','file'])
										}	
									}
								}
							}
						}
						session.clear()

					}
				}									
			}
		}
	}
		
	println "Done. Created XML export ${file.absolutePath}"
}
setDefaultTarget(main)
