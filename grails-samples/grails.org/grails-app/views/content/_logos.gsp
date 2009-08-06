<div id="springSourceBar">
	<div id="grailsLogo">
		<a href="http://grails.org/"><img src="${resource(dir:'images/new', file:'grailslogo_topNav.png')}" border="0"></a>			
	</div>

	<div id="springSourceLogo">
		<a href="http://www.springsource.com/"><img src="${resource(dir:'images/new', file:'springsource-logo.jpg')}" border="0"></a>			
	</div>
	<div id="searchbar">
		<g:form method="GET" url="[controller:'content', action:'search']" name="searchForm">
		    <input
		 		onblur="this.style.color = '#656565';" 
				onfocus="this.style.color = '#48802C';"
				type="text" accessKey="s" name="q" value="${params.q ?: ''}"/>		
		</g:form>		
	</div>
</div>
