<g:set var="permission" value="${new com.g2one.gtunes.AlbumPermission(album:album)}" />
<div id="album${album.id}" class="album" style="display:none;">
	<div class="albumArt">
		<music:albumArt artist="${artist}" album="${album}" />
	</div>
	<div class="albumDetails">


		<div class="artistName">${artist.name}</div>
		<div class="albumTitle">${album?.title}</div>
		<div class="albumInfo">
			Genre: ${album.genre ?: 'Other'}<br>
			Year: ${album.year}<br>
			<jsec:lacksPermission permission="${permission}">
				<strong>Price: $ ${album.price}</strong>
			</jsec:lacksPermission>
		</div>
		<div class="albumTracks">
			<ol>
				<g:each in="${album.songs}" var="song">
					<li><jsec:lacksPermission permission="${permission}">${song.title}</jsec:lacksPermission>
						<jsec:hasPermission permission="${permission}">
							<g:link controller="song" action="play" id="${song.id}">${song.title}</g:link>
						</jsec:hasPermission>
					</li>
				</g:each>
			</ol>
		</div>
		<div class="albumLinks">
			<g:remoteLink controller="artist" action="display" id="${artist.id}" update="musicPanel">More from this artist</g:remoteLink><br>
			<g:if test="${album.genre}">
				<g:remoteLink controller="store" action="search" params="[q:'+genre:'+album.genre]" update="musicPanel">Other albums you might like</g:remoteLink><br>							
			</g:if>
			<jsec:hasRole name="ADMINISTRATOR">
				<br />
				<h3>Administrator Functions</h3>
				<g:form controller="album" id="${album.id}">
					<g:actionSubmit value="New Album" action="create"></g:actionSubmit>				
					<g:actionSubmit value="Edit Album" action="edit"></g:actionSubmit>
					<g:actionSubmit value="Delete Album" action="delete" onclick="return confirm('Are you sure?');"></g:actionSubmit>					
				</g:form>
			</jsec:hasRole>
			
			<div id="buttons" style="float:right;">
				<jsec:hasPermission permission="${permission}">
					<g:link controller="user" action="music">Back to My Music</g:link>
				</jsec:hasPermission>
				<jsec:lacksPermission permission="${permission}">
					<g:link controller="store" action="buy" id="${album.id}"><img src="${createLinkTo(dir:'images',file:'buy-button.gif')}" border="0"></g:link>								
				</jsec:lacksPermission>
			</div>
		</div>
	</div>
</div>
<g:javascript>
	Effect.Appear($('album${album.id}'))
</g:javascript>