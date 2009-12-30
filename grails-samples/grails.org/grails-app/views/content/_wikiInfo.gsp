<g:set var="updateElement" value="${update ?: 'contentPane'}" />

<div id="infoLinks" class="wikiLinks">
    <ul class="wikiActionMenu">
        <li>
            <g:remoteLink class="actionIcon" controller="content" action="editWikiPage" id="${wikiPage?.title}" params="[update:updateElement]" update="${updateElement}" onLoaded="hideCommentPost()">
                <img border="0" src="${createLinkTo(dir:'images/','icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0" />
                <span>Edit</span>
            </g:remoteLink>
        </li>
        <li>
            <g:remoteLink class="actionIcon" update="${updateElement}" controller="content" id="${wikiPage?.title}" params="[xhr:true, update:updateElement]" onLoaded="showCommentPost()">
                <img src="${createLinkTo(dir:'images/','icon-info.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0" />
                <span>View Page</span>
            </g:remoteLink>
        </li>
    </ul>
</div>
<g:if test="${!update}">
    <h1>Page: ${wikiPage?.title}</h1>
</g:if>
<g:if test="${versions}">
<p>
    <strong>First Created:</strong> ${wikiPage?.dateCreated} by <strong>${first.author.login}</strong>
</p>
<p>
    <strong>Last Updated:</strong> ${wikiPage?.lastUpdated} by <strong>${last.author.login}</strong>
</p>


	<h3>Versions:</h3>
	<div id="versions">
		<cache:text id="${'versionList'+wikiPage?.id}">		
	    	<g:render template="versionList" model="[versions:versions,authors:authors, wikiPage:wikiPage, update:updateElement]" />
		</cache:text>
	</div>
	</g:if>
	<g:else>No Versions</g:else>

