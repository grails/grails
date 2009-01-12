<ul>
	<g:each in="${artists?}" var="artist">
		<li><g:remoteLink update="musicPanel" controller="artist" action="display" id="${artist?.id}">${artist?.name}</g:remoteLink></li>
	</g:each>
</ul>