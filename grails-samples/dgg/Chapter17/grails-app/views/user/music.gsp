<g:applyLayout name="libraryLayout">
	<div id="musicLibrary" class="musicLibrary">
		<g:if test="${!artists}">
			You haven't purchased any music just yet. Why don't you take a <g:link controller="store" action="shop">look at the store</g:link> to see what's available.
		</g:if>
		<g:each var="artist" in="${artists}">
			<div id="artist${artist.id}" class="artist">
				<h2>${artist.name}</h2>		
				<g:each var="album" in="${albums.findAll { it.artist.name == artist.name}}">
					<span class="purchasedAlbum">
						<g:remoteLink update="musicLibrary" controller="album" action="display" id="${album.id}">
							<music:albumArt width="100" artist="${artist.name}" album="${album.title}" alt="${album.title}"/>
						</g:remoteLink>
					</span>
				
				</g:each>
			</div>	
		</g:each>
	</div>
</g:applyLayout>