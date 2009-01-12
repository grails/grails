<div id="artist${artist.id}" class="artistProfile" style="display:none;">

	<div class="artistDetails">
		<h2>${artist.name}</h2>		
		<h3>Albums</h3>
		<g:each in="${albums}" var="album">
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
		<g:render template="subscribe" model="[artist:artist]"></g:render>
		
		<jsec:hasRole name="ADMINISTRATOR">
			<br />
			<h3>Administrator Functions</h3>
			<g:form controller="artist" id="${artist.id}">
				<g:actionSubmit value="New Artist" action="create"></g:actionSubmit>				
				<g:actionSubmit value="Edit Artist" action="edit"></g:actionSubmit>
				<g:actionSubmit value="Delete Artist" action="delete" onclick="return confirm('Are you sure?');"></g:actionSubmit>					
			</g:form>
		</jsec:hasRole>
	</div>
</div>
<g:javascript>
	Effect.Appear($('artist${artist.id}'))
</g:javascript>