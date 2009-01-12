<jsec:isLoggedIn>
	<p id="subscription">
		<g:isSubscribed artist="${artist}">
				<g:remoteLink update="subscription" 
					  controller="artist" 
					  action="unsubscribe" 
					  id="${artist.id}">Unsubscribe</g:remoteLink> - Click here to no longer receive e-mail updates when <strong>${artist.name}</strong> release a new album.											
		</g:isSubscribed>
		<g:notSubscribed artist="${artist}">
			<g:remoteLink update="subscription" 
				  controller="artist" 
				  action="subscribe" 
				  id="${artist.id}">Subscribe</g:remoteLink>	Click here to receive e-mail updates when <strong>${artist.name}</strong> release a new album.	
		</g:notSubscribed>
	</p>
</jsec:isLoggedIn>