<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8">
		<meta name="layout" content="main">
		<title>gTunes Store</title>
		<g:javascript library="prototype"></g:javascript>
		<style type="text/css" media="screen">
			.formField label {
				position:absolute;
			}
			.formField input {
				margin-left:150px;
			}
			#registerForm {
				margin-top:20px;
				width:400px;
			}
			.formButton {
				float:right;
				margin-top:30px;
			}
		</style>
	</head>
	<body id="body">
		<h1>Registration</h1>
		<p>Complete for the form below to create an account!</p>
		<g:if test="${user?.hasErrors() || message}">
			<div class="errors">
				<g:renderErrors bean="${user}"></g:renderErrors>
				<g:if test="${message}">
					<g:message code="${message}"></g:message>
				</g:if>
			</div>			
		</g:if>
		
		<g:form action="register" name="registerForm">
			<div class="formField">
				<label for="login">Login:</label>
				<g:textField name="login" value="${user?.login}"></g:textField>
			</div>
			<div class="formField">			
				<label for="password">Password:</label>
				<g:passwordField name="password" value="${user?.password}"></g:passwordField>			
			</div>
			<div class="formField">			
				<label for="confirm">Confirm Password:</label>
				<g:passwordField name="confirm" value="${params?.confirm}"></g:passwordField>			
			</div>
			<div class="formField">
				<label for="firstName">First Name:</label>
				<g:textField name="firstName" value="${user?.firstName}"></g:textField>
			</div>			
			<div class="formField">
				<label for="lastName">Last Name:</label>
				<g:textField name="lastName" value="${user?.lastName}"></g:textField>
			</div>	
			<g:submitButton class="formButton" name="register" value="Register"></g:submitButton>					
		</g:form>
	</body>
	
</html>