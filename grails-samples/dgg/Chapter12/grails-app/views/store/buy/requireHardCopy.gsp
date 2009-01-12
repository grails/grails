<g:applyLayout name="storeLayout">
	<div id="shoppingCart" class="shoppingCart">
		<h2>Would you like a CD edition of the album sent to you or a friend as a gift?</h2>
		<div class="choiceButtons">
			<g:link controller="store" action="buy"	event="yes"><img src="${createLinkTo(dir:'images',file:'yes-button.gif')}" border="0"/></g:link> 
			<g:link controller="store" action="buy" event="no"><img src="${createLinkTo(dir:'images',file:'no-button.gif')}" border="0"/></g:link>
		</div>
	</div>		
</g:applyLayout>
