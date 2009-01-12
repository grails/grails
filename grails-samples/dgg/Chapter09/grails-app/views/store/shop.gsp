<g:applyLayout name="storeLayout">
	<div id="musicPanel">
		<h2>Genres</h2>

		<ul class="genreList">
			<g:each var="genre" in="${genres}">
				<li class="genre"><g:link controller="store" action="genre" params="[name:genre]">${genre}</g:link></li>
			</g:each>			
		</ul>			
	</div>
</g:applyLayout>
