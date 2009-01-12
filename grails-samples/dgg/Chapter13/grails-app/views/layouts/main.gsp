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
				<g:render template="/user/welcomeMessage"></g:render>
			</g:if>
			<g:else>
				<g:render template="/user/loginForm"></g:render>				
			</g:else>
		</div>

        <div class="logo">
			<img src="${createLinkTo(dir:'images',file:'gTunes-logo.jpg')}" alt="Grails" />
		</div>	
		<div id="navPane">
			<div id="navButtons" style="display:${session.user? 'block' :'none'}">
				<ul>
					<li><a href="#">My Music</a></li>
					<li><g:link controller="store" action="shop">The Store</g:link></li>				
				</ul>
			</div>
			<div id="registerPane" style="display:${session.user? 'none' :'block'}">
				Need an account? <g:link controller="user" action="register">Signup now</g:link> to start your own personal Music collection!
			</div>			

		</div>
	
		<div id="contentPane">
        	<g:layoutBody />					
		</div>

    </body>	
</html>