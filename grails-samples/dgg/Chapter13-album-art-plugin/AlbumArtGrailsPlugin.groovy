class AlbumArtGrailsPlugin {
    def version = 0.1
	def dependsOn = [simpleCache:'0.1 > *']
	
    def author = "Graeme Rocher"
    def authorEmail = "graeme@g2one.com"
    def title = "A plug-in that provides facilities to look-up album art"
    def description = '''\
A plug-in that provides facilities to look-up album art
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/AlbumArt+Plugin"

    def doWithDynamicMethods = { ctx ->
		def albumArtService = ctx.getBean("albumArtService")
		
		application.controllerClasses*.metaClass*.getAlbumArt = { String artist, String album ->
			return albumArtService.getAlbumArt(artist, album) 
		}
		
		def albumClass = application.domainClasses.find { it.shortName == 'Album' }
		if(albumClass) {
			albumClass.metaClass.getArt ={-> albumArtService.getAlbumArt(  delegate.artist?.name,
																		   delegate.title)  }
		}
    }
	
}
