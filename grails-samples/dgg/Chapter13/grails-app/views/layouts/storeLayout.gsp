<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8">
		<meta name="layout" content="main">
		<title>gTunes Store</title>
	</head>
	<body id="body">
		<h1>Online Store</h1>
		<p>Browse or search the categories below:</p>
		<g:render template="/store/searchbox" />
		<g:render template="/store/top5panel" model="${pageScope.variables}" />
		<div id="musicPanel">
			<g:layoutBody />
		</div>
	</body>
	
</html>