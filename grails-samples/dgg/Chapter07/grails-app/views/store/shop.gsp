<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8">
		<meta name="layout" content="main">
		<title>gTunes Store</title>
		<g:javascript library="prototype"></g:javascript>
	</head>
	<body id="body">
		<h1>Online Store</h1>
		<p>Browse the categories below:</p>			
		<h2>Genres</h2>
		<ul class="genreList">
			<g:each var="genre" in="${genres}">
				<li class="genre"><g:link controller="store" action="genre" params="[name:genre]">${genre}</g:link></li>
			</g:each>			
		</ul>
	</body>
	
</html>