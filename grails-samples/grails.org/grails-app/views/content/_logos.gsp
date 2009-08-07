<div id="springSourceBar">
	<div id="grailsLogo">
		<a href="/"><img src="${resource(dir:'images/new', file:'grailslogo_topNav.png')}" border="0"></a>			
	</div>

	<div id="springSourceLogo">
		<a href="http://www.springsource.com/"><img src="${resource(dir:'images/new', file:'springsource-logo.jpg')}" border="0"></a>			
	</div>
	<div id="searchbar">
		<g:form method="GET" url="[controller:'content', action:'search']" name="searchForm">
		    <input
		 		onblur="this.style.color = '#CDE5C4';" 
				onfocus="this.style.color = '#48802C';this.value='';"
				type="text" accessKey="s" name="q" value="${params.q ?: 'Search Grails.org'}"/>		
		</g:form>		
	</div>
</div>
