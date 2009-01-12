<ul>
	<g:each in="${songs?}" var="song">
		<li><g:remoteLink update="musicPanel" 
						  controller="song" 
						  action="display" 
						  id="${song?.id}">${song.title}</g:remoteLink></li>
	</g:each>
</ul>