<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8">
		<title>Test Layout</title>		
	</head>
	<body id="test">
		<h1 id="children">Children</h1>
		<g:each var="child" in="${test.children}">
			<p>${child.name}</p>
		</g:each>
		<g:layoutBody />
	</body>
</html>