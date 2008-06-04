<ul>
    <g:each var="mirror" in="${downloadFile.mirrors}">
        <li><a href="${mirror.url}">${mirror.name} (${mirror.url})</a> | <g:remoteLink update="mirrors" action="deleteMirror" id="${mirror.id}">Delete</g:remoteLink></li>
    </g:each>
</ul>
