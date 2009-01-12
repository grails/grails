<g:applyLayout name="libraryLayout">
	<div id="musicLibrary" class="musicLibrary">
		<div class="songPlayer">
			<h2>${song.artist.name} - ${song.title}</h2>
			<div class="albumArt">
				<music:albumArt artist="${song.artist.name}" album="${song.album.title}"  />
			</div>
			<div class="player">
				<media:player src="${createLink(controller:'song', action:'stream', id:song.id)}" autoplay="true" height="20" width="200" />							
			</div>			
			<div class="links" style="float:right;">
					<g:remoteLink controller="album" action="display" id="${song.album.id}" update="musicLibrary">Back to Album</g:remoteLink><br>								
					<g:link controller="user" action="music">Back to My Music</g:link><br>				
			</div>
		</div>
	</div>
</g:applyLayout>
		