<g:set var="updateElement" value="${update ?: 'contentPane'}" />
<g:if test="${message}">
    <div id="message" class="message">${message}</div>
</g:if>
<ul>

<g:each in="${versions}" var="v" status="i">
    <li>
        <g:remoteLink update="${updateElement}"
                controller="content"
                action="showWikiVersion" id="${wikiPage?.title}"
                params="[number:v, update:updateElement]">
            Version ${v}</g:remoteLink> (Updated by <strong>${authors[i].login}</strong>)

        <jsec:authenticated>
            <g:if test="${v != wikiPage.version}">
                <jsec:hasRole name="Administrator">
                    <g:remoteLink update="versions"
                            controller="content"
                            action="rollbackWikiVersion"
                            id="${wikiPage?.title}"
                            params="[number:v]">Rollback to here</g:remoteLink>
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
                        params="[number:v,diff:previous]"
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