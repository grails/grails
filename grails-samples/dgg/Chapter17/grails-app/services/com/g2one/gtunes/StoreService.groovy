package com.g2one.gtunes

class StoreService {
	
	static transactional = true
	
	def mailService
	
	void onNewAlbum(Album album) {				
		try {
			def artist = album.artist
			def users = ArtistSubscription.withCriteria {
				projections {
					property "user"
				}
				eq('artist', artist)
			}

			for(user in users) {
				mailService.sendMail {
					from "notifications@gtunes.com"
					to user.email
					title "gTunes Notifcation: The artist ${artist.name} has released a new album called ${album.title}!"
					body view:"/emails/artistSubscription", model:[album:album, artist:artist, user:user]
				}
			}
			
		}
		catch(Exception e) {
			log.error "Error sending album $album notification message: $e.message", e
			throw e
		}		
	}
	
	Payment purchaseAlbums(User user, creditCard, List albumPayments) {

		def p = new Payment(user:user)
				
		p.invoiceNumber = "INV-${user.id}-${System.currentTimeMillis()}"								
 		if(!creditCard.validate()) {
			throw new IllegalStateException("Credit card must be valid")
		}
		// TODO: Use credit card to take payment
		// ...
		
		// Once payment taken update user profile
		for(ap in albumPayments) {
			ap.user = user
			// validation should never fail at this point
			if(!ap.validate()) {
				throw new IllegalStateException("Album payment must be valid")				
			}

			p.addToAlbumPayments(ap)
			if(!p.save(flush:true)) {
				throw new IllegalStateException("Payment must be valid")								
			}
			
			ap.album.songs.each { user.addToPurchasedSongs(it) }						
			user.addToPurchasedAlbums(ap.album)
			user.addToPermissions(new AlbumPermission(album:ap.album))		
		}	
		if(!user.save(flush:true)) {
			throw new IllegalStateException("User must be valid")			
		}
		return p
	
	}
}