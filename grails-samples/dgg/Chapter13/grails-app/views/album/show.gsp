<g:applyLayout name="storeLayout">
	<g:if test="${params.message}">
		<div class="message">
			<g:message code="${params.message}"></g:message>
		</div>
	</g:if>
	<h2>Album: ${album.title}</h2>	
	<g:render template="album" model="[album:album]"></g:render>
</g:applyLayout>