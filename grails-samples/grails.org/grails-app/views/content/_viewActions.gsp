<div id="viewLinks" style="margin-left:500px;">

    <g:if test="${content?.locked}">
        LOCKED<br/>
    </g:if>
    <g:else>
        <g:remoteLink class="actionIcon" action="editWikiPage" id="${content?.title}" update="contentPane">
            <img border="0" src="${createLinkTo(dir:'images/','icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0" />
        </g:remoteLink>
        <g:remoteLink action="editWikiPage" id="${content?.title}" update="contentPane">Edit</g:remoteLink>
    </g:else>

    <g:remoteLink class="actionIcon" action="infoWikiPage" id="${content?.title}" update="contentPane">
        <img border="0" src="${createLinkTo(dir:'images/','icon-info.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0" />
    </g:remoteLink>
    <g:remoteLink action="infoWikiPage" id="${content?.title}" update="contentPane">View Info</g:remoteLink>

</div>
