package com.g2one.gtunes

class SongControllerTests extends grails.test.ControllerUnitTestCase {
	
	
	void testPlaySongNotFound() {
		mockDomain(Song)
		controller.play()
		
		assertEquals 404, controller.response.status
	}
	
	void testPlaySoundFound() {
		mockDomain(Song,[new Song(id:10, title:"Blood")])
		controller.metaClass.getJsec = {->
			[ hasPermission:{ Map m, Closure c -> c.call()  },
			  lacksPermission:{ Map m, Closure c ->  } ]			
		}

		controller.params.id = 10
		controller.play()		
		
		assertEquals "play", controller.renderArgs.view
		assertEquals "Blood", controller.renderArgs.model?.song?.title		
	}
	
	void testStreamNotFound() {
		mockDomain(Song)
		controller.stream()
		
		assertEquals 404, controller.response.status		
	}
	
	void testStreamNoPermission() {
		mockDomain(Song,[new Song(id:10, title:"Blood")])
		controller.metaClass.getJsec = {->
			[ hasPermission:{ Map m, Closure c ->  },
			  lacksPermission:{ Map m, Closure c -> c.call() } ]			
		}

		controller.params.id = 10

		controller.stream()
		
		assertEquals 401, controller.response.status
		
	}
	
	void testStreamHasPermission() {
		mockDomain(Song,[new Song(id:10, title:"Blood", file:"/some/path.mp3")])
		controller.metaClass.getJsec = {->
			[ hasPermission:{ Map m, Closure c ->  c.call() },
			  lacksPermission:{ Map m, Closure c ->  } ]			
		}

		controller.params.id = 10

		File.metaClass.readBytes = {-> new byte[10] }
		controller.stream()		
		
		assertEquals "audio/x-mp3", controller.response.contentType
		assertTrue controller.response.isCommitted()
	}
	
	void tearDown() {
		GroovySystem.metaClassRegistry.removeMetaClass(File)
	}
}