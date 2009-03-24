<html>
    <head>
        <title>Welcome to Grails</title>
		<meta name="layout" content="main" />
    </head>
    <body>
       <g:uploadForm name="myForm" action="testUpload">
			<input type="file" name="myFile" /><br />
			<g:submitButton name="Upload"></g:submitButton>
		</g:uploadForm>
    </body>
</html>