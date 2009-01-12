<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8">
		<meta name="layout" content="main">
		<title>gTunes Store</title>
		<g:javascript library="prototype"></g:javascript>
	</head>
	<body id="body">
		<h1>Online Store</h1>
		<p>Browse or search the categories below:</p>
		<div id="searchBox">
			Instant Search: <g:remoteField name="q" update="musicPanel" paramName="q" url="[controller:'store', action:'search']"></g:remoteField>
		</div>
	

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
		<div id="musicPanel">
			<h2>Genres</h2>
			<ul class="genreList">
				<g:each var="genre" in="${genres}">
					<li class="genre"><g:link controller="store" action="genre" params="[name:genre]">${genre}</g:link></li>
				</g:each>			
			</ul>			
		</div>
	</body>
	
</html>