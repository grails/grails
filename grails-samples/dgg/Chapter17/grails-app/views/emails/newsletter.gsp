<html>
	<head>
	</head>
	<body id="newsletter">
		<h1>The gTunes Month Newsletter for ${month}</h1>
		<p>Welcome to the gTunes monthly newsletter! Below are the latest releases available at <g:createLink controller="store" absolute="true" /></p>
		<h2>Latest Album Releases</h2>
		<g:each var="album" in="${latestAlbums}">
			<div class="albumArt">
				<b>Title</b>: <g:link controller="album" action="show" id="${album.id}">${album.title}</g:link> <br>
				<b>Artist</b>: ${album.artist.name} <br>
				<div class="albumArt">
					<music:albumArt album="${album.title}" artist="${album.artist.name}">
				</div>
			</div>
		</g:each>
	</body>
</html>