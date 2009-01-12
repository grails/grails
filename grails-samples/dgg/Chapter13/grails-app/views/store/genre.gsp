<g:applyLayout name="storeLayout">
	<div id="musicPanel">
		<h2>Genre: ${genre.encodeAsHTML()}</h2>
		<table border="0" class="albumsTable">
			<tr>
				<th>Artist</th>
				<th>Album</th>										
				<th>Year</th>															
			</tr>
			<g:each var="album" in="${albums}">
				<tr>
					<td>${album.artist.name}</td>
					<td><g:link action="show" controller="album" id="${album.id}">${album.title}</g:link></td>										
					<td>${album.year}</td>																
				</tr>
			</g:each>
		
		</table>
		<div class="paginateButtons">
			<g:paginate controller="store" action="genre" params="[name:genre]" total="${totalAlbums}" />			
		</div>

		<div style="margin-top:10px">
			<g:link controller="store" action="shop">Back to Store</g:link>
		</div>			
	</div>
</g:applyLayout>

