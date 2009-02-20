<g:set var="updateElement" value="${update ?: 'contentPane'}" />
<g:if test="${message}">
    <div id="message" class="message">${message}</div>
</g:if>
<ul>

<g:each in="${versions}" var="v">
    <li>
        <g:remoteLink update="${updateElement}"
                controller="content"
                action="showWikiVersion" id="${wikiPage?.title}"
                params="[number:v.number, update:updateElement]">
            Version ${v.number}</g:remoteLink> (Updated by <strong>${v.author.login}</strong>)

        <jsec:authenticated>
            - Created: ${v.dateCreated} -
            <g:if test="${v.number != wikiPage.version}">
                <jsec:hasRole name="Administrator">
                    <g:remoteLink update="versions"
                            controller="content"
                            action="rollbackWikiVersion"
                            id="${wikiPage?.title}"
                            params="[number:v.number]">Rollback to here</g:remoteLink>
                </jsec:hasRole>
            </g:if>
            <g:else>Latest Version</g:else>
            |
            <g:if test="${previous}">
                <g:remoteLink update="diffPane"
                        controller="content"
                        action="diffWikiVersion"
                        id="${wikiPage?.title}"
                        options="[method:'POST']"
                        params="[number:v.number,diff:previous.number]"
                        onComplete="showDiff();">Diff with previous</g:remoteLink></li>
            </g:if>
            <g:else>
                First Version
            </g:else>
        </jsec:authenticated>

        <g:set var="previous" value="${v}" />
</g:each>
</ul>
<div id="diffPane">
</div>
<div id="diffOutputDiv" class="diffOutput" style="display:none;">
    
</div>
<script type="text/javascript">
    if($('message')!=null) {
        Effect.Fade('message', {delay:3})
    }
</script>