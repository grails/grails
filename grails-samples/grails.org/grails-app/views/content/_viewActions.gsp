<div id="viewLinks" style="margin-left:500px;">

    <g:set var="updateElement" value="${update ?: 'contentPane'}"/>

    <g:if test="${content?.locked}">
        LOCKED<br/>
    </g:if>
    <g:else>
        <g:remoteLink class="actionIcon" controller="content" action="editWikiPage" id="${content?.title}" params="[update:updateElement]" update="${updateElement}">
            <img border="0" src="${createLinkTo(dir:'images/','icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0" />
            <span>Edit</span>
        </g:remoteLink>
    </g:else>
    <g:remoteLink class="actionIcon" controller="content" action="infoWikiPage" id="${content?.title}" params="[update:updateElement]" update="${updateElement}">
        <img border="0" src="${createLinkTo(dir:'images/','icon-info.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0" />
        <span>View Info</span>
    </g:remoteLink>

</div>
