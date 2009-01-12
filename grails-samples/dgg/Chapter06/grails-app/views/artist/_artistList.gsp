<ul>
	<g:each in="${artists?}" var="artist">
		<li><g:remoteLink update="musicPanel" controller="artist" action="display" params="[artistName:artist.name.replaceAll(' ', '_')]">${artist?.name}</g:remoteLink></li>
	</g:each>
</ul>