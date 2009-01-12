<div id="album${album.id}" class="album">
	<div class="albumDetails">
		<div class="artistName">${artist.name}</div>
		<div class="albumTitle">${album.title}</div>
		<div class="albumInfo">
			Genre: ${album.genre ?: 'Other'}<br>
			Year: ${album.year}
		</div>
		<div class="albumTracks">
			<ol>
				<g:each in="${album.songs}" var="song">
					<li>${song.title}</li>
				</g:each>
			</ol>
		</div>
	</div>
</div>
