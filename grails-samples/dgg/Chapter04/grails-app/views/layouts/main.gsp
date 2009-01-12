<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
		<link rel="stylesheet" href="${createLinkTo(dir:'css',file:'gtunes.css')}" type="text/css" media="screen" charset="utf-8">
        <g:javascript library="scriptaculous" />			
        <g:javascript library="application" />	

    </head>
    <body>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${createLinkTo(dir:'images',file:'spinner.gif')}" alt="Spinner" />
        </div>	
		
		<div id="loginBox" class="loginBox">
			<g:if test="${session?.user}">
				<div style="margin-top:20px">
					<div style="float:right;">
						<a href="#">Profile</a> | <g:link controller="user" action="logout">Logout</g:link><br>						
					</div>

					Welcome back <span id="userFirstName">${session?.user?.firstName}!</span><br><br>

					You have purchased (${session.user.purchasedSongs?.size() ?: 0}) songs.<br>
				</div>
			</g:if>
			<g:else>

				<g:form 
					name="loginForm" 
					url="[controller:'user',action:'login']">
					<div>Username:</div>
					<g:textField name="login" value="${fieldValue(bean:loginCmd, field:'login')}"></g:textField>
					<div>Password:</div>
					<g:passwordField name="password"></g:passwordField>			
					<br/>
					<input 	type="image" 
						 	src="${createLinkTo(dir:'images', file:'login-button.gif')}" 
							name="loginButton" id="loginButton" border="0"></input>	
				</g:form>
				<g:renderErrors bean="${loginCmd}"></g:renderErrors>

			</g:else>
		</div>

        <div class="logo">
			<img src="${createLinkTo(dir:'images',file:'gTunes-logo.jpg')}" alt="Grails" />
		</div>	
		<div id="navPane">
			<g:if test="${session.user}">
				<ul>
					<li><g:link controller="user" action="music">My Music</g:link></li>
					<li><g:link controller="store" action="shop">The Store</g:link></li>				
				</ul>			
			</g:if>
			<g:else>
				<div id="registerPane">
					Need an account? <g:link controller="user" action="register">Signup now</g:link> 
					to start your own personal Music collection!
				</div>						
			</g:else>
		</div>
	
		<div id="contentPane">
        	<g:layoutBody />					
		</div>

    </body>	
</html>