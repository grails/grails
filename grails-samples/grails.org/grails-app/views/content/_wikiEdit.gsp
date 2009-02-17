<g:render template="editActions" model="[content:wikiPage]"/>

<div id="uploadDialog" class="dialog" style="display:none;margin-top:10px;width:500px;">
    <iframe id="uploadIframe" width="550" height="40" frameborder="0" scrolling="no" src="${createLink(controller: 'content', action: 'uploadImage', id: wikiPage.title)}"></iframe>
</div>

<div id="editForm" style="margin-top:10px;">
    <g:render template="/common/messages" model="${pageScope.getVariables() + [bean:wikiPage]}"/>

    <g:if test="${!wikiPage.locked}">
        <g:formRemote name="wikiEditForm" url="[controller:'content',action:'saveWikiPage',id:wikiPage.title]"
                method="post" update="editPane"
                onComplete="\$('editButton').style.display='inline';">
            <g:render template="wikiFields" model="[wikiPage:wikiPage]"/>
        </g:formRemote>
    </g:if>

    <g:render template="/common/messages_effects" model="${pageScope.getVariables()}"/>
</div>

