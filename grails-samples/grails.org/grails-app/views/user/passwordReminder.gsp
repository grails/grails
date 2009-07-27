<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8">
		<meta name="layout" content="subpage">
		<title>Password Reminder</title>
		
	</head>
	
	<body id="passwordreminder">
		<p>Enter the username you registered with and your password will be emailed to you:</p>
		<g:if test="${flash.message}">
			<div class="error">
				${flash.message}				
			</div>

		</g:if>
		<g:form action="passwordReminder" class="userForm">

            <p>
                 <span class="label"><label for="login">Username:</label></span> <g:textField class="textInput" name="login" value="${formData?.login}"/>
             </p>
			<g:submitButton name="Send Password"></g:submitButton>
			
		</g:form>
	</body>
</html>