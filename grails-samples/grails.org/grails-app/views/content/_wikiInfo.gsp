<div id="infoLinks" style="margin-left:500px;">
    <g:remoteLink class="actionIcon" action="editWikiPage" id="${wikiPage?.title}" update="contentPane">
        <img border="0" src="${createLinkTo(dir:'images/','icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0" />
    </g:remoteLink>
    <g:remoteLink action="editWikiPage" id="${wikiPage?.title}" update="contentPane">Edit</g:remoteLink>
    
    <g:remoteLink update="contentPane" controller="content" id="${wikiPage?.title}" params="[xhr:true]"><img src="${createLinkTo(dir:'images/','icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0" /></g:remoteLink>
    <g:remoteLink update="contentPane" controller="content" id="${wikiPage?.title}" params="[xhr:true]">View Page</g:remoteLink>
</div>

<h1>Page: ${wikiPage?.title}</h1>
<p>
    <strong>First Created:</strong> ${wikiPage?.dateCreated} by <strong>${versions[0].author.login}</strong>
</p>
<p>
    <strong>Last Updated:</strong> ${wikiPage?.lastUpdated} by <strong>${versions[-1].author.login}</strong>
</p>

<h3>Versions:</h3>
<div id="versions">
    <g:render template="versionList" model="[versions:versions, wikiPage:wikiPage]" />
</div>
