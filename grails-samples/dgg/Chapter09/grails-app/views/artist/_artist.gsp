<div id="artist${artist.id}" class="artistProfile" style="display:none;">

	<div class="artistDetails">
		<h2>${artist.name}</h2>		
		<h3>Albums</h3>
		<g:each in="${albums?}" var="album">
			<div class="artistAlbum">
				<g:remoteLink controller="album" action="display" update="musicPanel" id="${album.id}">
					<music:albumArt width="100" album="${album}" artist="${artist}"></music:albumArt>
				</g:remoteLink>
				<ul>
					<li><strong>Title:</strong> ${album.title} </li>
					<li><strong>Genre:</strong> ${album.genre}</li>
					<li><strong>Year:</strong> ${album.year}</li>
					<li><strong>Track Count:</strong> ${album.songs?.size()}</li>
				</ul>
			</div>			
		</g:each>

	</div>
</div>
<g:javascript>
	Effect.Appear($('artist${artist.id}'))
</g:javascript>