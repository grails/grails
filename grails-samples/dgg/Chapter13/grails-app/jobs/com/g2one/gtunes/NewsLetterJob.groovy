package com.g2one.gtunes


class NewsLetterJob {
	// fire at 7:15am on the last Thursday of every mont
    def cronExpression = "0 15 7 ? * 5L"
	

	def mailService
    def execute() {		
		def emails = Subscription.withCriteria {
			projections {
				user {
					property 'email'
				}
			}
			eq('type', SubscriptionType.NEWSLETTER)
		}
		
		def now = new Date()
		def albumList = Album.findAllByDateCreatedBetween(now-7,now, [sort:'dateCreated', max:10,order:'desc'])
		if(albumList) {
			def month = new GregorianCalendar().get(Calendar.MONTH)
			for(email in emails) {
				mailService.sendMail {
					from "info@gtunes.com"
					to email
					title "The gTunes Monthly News Letter - $month"
					body view:"/emails/newsletter",
						 model:[latestAlbums:albumList,
								month:month]
				}
			}			
		}
    }
}
