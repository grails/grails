<div id="viewLinks" class="wikiLinks">
    <g:set var="updateElement" value="${update ?: 'contentPane'}"/>

    <ul class="wikiActionMenu">
        <g:if test="${content?.locked}">
            <li>LOCKED</li>
        </g:if>
        <g:else>
            <li>
                <g:remoteLink class="actionIcon" controller="content" action="editWikiPage" id="${content?.title}" params="[update:updateElement, editFormName:editFormName]" update="${updateElement}" onLoaded="hideCommentPost()">
                    <img border="0" src="${createLinkTo(dir: 'images/', 'icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0"/>
                    <span>Edit</span>
                </g:remoteLink>
            </li>
        </g:else>
        <li>
            <g:remoteLink class="actionIcon" controller="content" action="infoWikiPage" id="${content?.title}" params="[update:updateElement]" update="${updateElement}">
                <img border="0" src="${createLinkTo(dir: 'images/', 'icon-info.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0"/>
                <span>View Info</span>
            </g:remoteLink>
        </li>
    </ul>
</div>