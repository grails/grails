<ul>
	<g:each in="${albums?}" var="album">
		<li><g:remoteLink update="musicPanel" 
						  controller="album" 
						  action="display" 
						  id="${album.id}">${album.title}</g:remoteLink></li>
	</g:each>
</ul>
