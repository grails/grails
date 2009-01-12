package com.g2one.gtunes

class StoreController {

	StoreService storeService
	
    def index = {
    }

	def shop = {
		def genreList = Album.withCriteria {
			projections {
				distinct "genre"
			}
		}
		
		
		[
			genres:genreList.sort()
		] 
	}
	
	private addAlbumToCartAction = { 
		if(!flow.albumPayments) flow.albumPayments = []
		def album = Album.get(params.id) 

		if(!flow.albumPayments.album.find { it?.id == album.id }) {
			flow.lastAlbum = new AlbumPayment(album:album)
			flow.albumPayments << flow.lastAlbum
		}	
		flow.genreRecommendations = null
		flow.userRecommendations = null
	}
	
	def buyFlow = {
		start {
			action {
				// check login status
				if(!params.id) {
					return showStore()
				}
				if(request.user) {
					flow.user = User.get(request.user.id)
					return success()
				} 
				login()
			}
			on('success', addAlbumToCartAction).to 'requireHardCopy'
			on('login') {
				flash.album = Album.get(params.id)
				flash.message = "user.not.logged.in"
			}.to 'requiresLogin'
			on('showStore').to 'showStore'
		}
		requireHardCopy {
			on('yes') {
				if(!flow.shippingAddresses)
					flow.shippingAddress = new Address()
			}.to 'enterShipping'
			on('no') {
				flow.shippingAddress = null
			}.to 'loadRecommendations'
		}
		enterShipping {
			on('next') {
				def address = flow.shippingAddress
				address.properties = params
				if(address.validate()) {
					flow.lastAlbum.shippingAddress = address 
					return success()
				} 
				return error()
			}.to 'loadRecommendations'
			on('back') {
				flow.shippingAddress.properties = params
			}.to 'requireHardCopy'			
		}
		loadRecommendations {
			action {

				if(!flow.genreRecommendations) {					
					def albums = flow.albumPayments.album
					def genres = albums.genre
					flow.genreRecommendations = Album.withCriteria {
						inList 'genre', genres
						not {
							inList 'id', albums.id
						}
						maxResults 4
						order 'dateCreated', 'desc'
					}
				}
				if(!flow.userRecommendations) {
					def albums = flow.albumPayments.album

					def otherAlbumPayments = AlbumPayment.withCriteria {
						user {
							purchasedAlbums {
								inList 'id', albums.id									
							}
						}
						not {
							eq 'user', flow.user
							inList 'album', albums
						}
						maxResults 4
					}
					
					flow.userRecommendations = otherAlbumPayments.album
		
				}
				if(!flow.genreRecommendations && !flow.userRecommendations) {
					return error()
				}
								
			}
			on('success').to 'showRecommendations'
			on('error').to 'enterCardDetails'
			on(Exception).to 'enterCardDetails'
		}
		showRecommendations {
			on('addAlbum', addAlbumToCartAction).to 'requireHardCopy'
			on('next').to 'enterCardDetails'
			on('back').to { flow.shippingAddress ? 'enterShipping' : 'requireHardCopy' }
		}
		enterCardDetails {
			on('next') { CreditCardCommand cmd ->
				flow.creditCard = cmd
				cmd.validate() ? success() : error()
			}.to 'showConfirmation'
			on('back').to { 
				def view
				if(flow.genreRecommendations || flow.userRecomendations)
					view = "showRecommendations"
				else if(flow.lastAlbum.shippingAddress) {
					view = 'enterShipping'
				}
				else {
					view = 'requireHardCopy'
				}
				return view
			}
		}
		showConfirmation {
			on('confirm') {
				// NOTE: Dummy implementation of transaction processing, a real system would
				// integrate an e-commerce solution
				def user = flow.user
				def albumPayments = flow.albumPayments
				flow.payment = storeService.purchaseAlbums(user, flow.creditCard, flow.albumPayments)				
			}.to 'displayInvoice'
			on('back').to 'enterCardDetails'
			on('error').to 'displayError'
			on(Exception).to 'displayError'
			
		}
		requiresLogin {
			redirect(controller:"album", action:"display", id: flash.album.id, params:[message:flash.message])
		}
		showStore {
			redirect(controller:"store", action:"shop")
		}
		displayInvoice()
		displayError()
	}
	
	def genre = {
		def max =params.max?.toInteger() ?: 10
		def total = Album.countByGenre(params.name)
		def albumList = Album.findAllByGenre(params.name, [offset:params.offset, 
														   max:max <= 100 ? max : 10, 
														   fetch:[artist:'join']])


		return [albums:albumList.sort { it.artist.name },
			    totalAlbums:total, 
				genre:params.name]
	}
	
	

	def search = {
		def q = params.q ?: null
		def searchResults
		if(q) {
			searchResults = [
				albumResults: trySearch { Album.search(q, [max:10]) },
				artistResults: trySearch { Artist.search(q, [max:10]) },
				songResults: trySearch { Song.search(q, [max:10]) },								
				q: q.encodeAsHTML()
			]
		} 
		
		render(template:"searchResults", model: searchResults)
	}

	def trySearch(Closure callable) {
		try {
			return callable.call()
		}
		catch(Exception e) {
			log.debug "Search Error: ${e.message}", e
			return []
		}
	}
	

}
class CreditCardCommand implements Serializable {
	String name
	String number
	String expiry
	Integer code
	static constraints = {
		name blank:false, minSize:3
		number creditCard:true, blank:false
		expiry matches:/\d{2}\/\d{2}/, blank:false
		code nullable:false,max:999
	}
}
