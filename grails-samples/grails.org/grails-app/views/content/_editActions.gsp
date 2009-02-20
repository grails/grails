<g:set var="updateElement" value="${update ?: 'contentPane'}"/>

<div id="editLinks" style="margin-left:230px;">

    <g:set var="href" value="${update ? 'javascript:void(0)' : '#'}"/>
    <a href="${href}" class="actionIcon" onclick="Effect.Appear('uploadDialog')">
        <img src="${createLinkTo(dir: 'images/', 'icon-upload.png')}" width="15" height="15" alt="Icon Image Upload" class="inlineIcon" border="0"/>
        <span>Upload Image</span>
    </a>

    <a href="${href}" class="actionIcon"
            onclick="new Ajax.Updater('previewPane', '${createLink(controller:'content',action:'previewWikiPage',id:content?.title)}', getAjaxOptions(function() {showPreview()})); return false">
        <img src="${createLinkTo(dir: 'images/', 'icon-preview.png')}" width="18" height="15" alt="Icon Edit" class="inlineIcon" border="0"/>
        <span>Preview</span>
    </a>

    <a href="${href}" class="actionIcon"
            onclick="new Ajax.Updater('${updateElement}', '${createLink(controller:'content',action:'saveWikiPage',id:content?.title, params:[update:updateElement])}', getAjaxOptions()); return false">
        <img src="${createLinkTo(dir: 'images/', 'icon-save.png')}" width="15" height="15" alt="Icon Save" class="inlineIcon" border="0"/>
        <span>Save</span>
    </a>

    <g:remoteLink class="actionIcon" update="${updateElement}" controller="content" id="${content?.title}" params="[xhr:true,update:updateElement]">
        <img src="${createLinkTo(dir: 'images/', 'icon-cancel.png')}" width="15" height="15" alt="Icon Cancel" class="inlineIcon" border="0"/>
        <span>Cancel</span>
    </g:remoteLink>

    <g:remoteLink class="actionIcon" action="infoWikiPage" id="${content?.title}" params="[update:updateElement]" update="${updateElement}">
        <img border="0" src="${createLinkTo(dir: 'images/', 'icon-info.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0"/>
        <span>View Info</span>
    </g:remoteLink>
</div>