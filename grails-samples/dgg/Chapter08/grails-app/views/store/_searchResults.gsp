<div id="searchResults" class="searchResults">
	
	<g:if test="${albumResults?.results}">
		<div id="albumResults" class="resultsPane">
			<h2>Album Results</h2>
			<g:render template="/album/albumList" model="[albums:albumResults.results]"></g:render>

		</div>		
	</g:if>
	
	<g:if test="${artistResults?.results}">
		<div id="artistResults" class="resultsPane">
			<h2>Artist Results</h2>
			<g:render template="/artist/artistList" model="[artists:artistResults.results]"></g:render>
		</div>
	</g:if>
	
	<g:if test="${songResults?.results}">
		<div id="songResults" class="resultsPane">
			<h2>Song Results</h2>			
			<g:render template="/song/songList" model="[songs:songResults.results]"></g:render>
		</div>
		
	</g:if>
</div>