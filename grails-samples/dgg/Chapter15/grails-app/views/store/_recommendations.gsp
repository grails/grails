<table class="recommendations">
	<tr>
		<g:each in="${albums?}" var="album" status="i">
			<td>
			<div id="rec${i}" class="recommendation">
				<g:set var="albumHeader">${album.artist.name} - ${album.title}</g:set>
				<p>${albumHeader.size() >15 ? albumHeader[0..15] + '...' : albumHeader}</p>
				<music:albumArt width="100" album="${album}" artist="${album.artist}" />
				<p><g:link controller="store" 
					       action="buy" 
					       id="${album.id}" 
					       event="addAlbum">Add to Purchase</g:link></p>
			</div>
			</td>
		</g:each>		
	</tr>
</table>