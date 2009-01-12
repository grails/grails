<div id="top5Panel" class="top5Panel">
	<h2>Latest Albums</h2>			
	<div id="albums" class="top5Item">
		<g:render template="/album/albumList" model="[albums:top5Albums]"></g:render>
	</div>
	<h2>Latest Songs</h2>
	<div id="songs" class="top5Item">
		<g:render template="/song/songList" model="[songs:top5Songs]"></g:render>
	</div>
	<h2>Newest Artists</h2>
	<div id="artists" class="top5Item">
		<g:render template="/artist/artistList" model="[artists:top5Artists]"></g:render>
	</div>			
</div>
