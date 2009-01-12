<g:applyLayout name="storeLayout">
	<div id="shoppingCart" class="shoppingCart">
		<h2>Album Recommendations</h2>
		
		<g:if test="${genreRecommendations}">
			<h3>Other music you might like...</h3>
			<g:render template="/store/recommendations" model="[albums:genreRecommendations]"></g:render>
			
		</g:if>
		<g:if test="${userRecommendations}">
			<h3>Other users who bought ${albumPayments.album} also bought...</h3>
			<g:render template="/store/recommendations" model="[albums:userRecommendations]"></g:render>			
		</g:if>
		<div class="formButtons">
			<g:link controller="store" action="buy" event="back"><img src="${createLinkTo(dir:'images',file:'back-button.gif')}" border="0"></g:link>
			<g:link controller="store" action="buy" event="next"><img src="${createLinkTo(dir:'images',file:'next-button.gif')}" border="0"></g:link>									
		</div>
		
	</div>		
</g:applyLayout>
