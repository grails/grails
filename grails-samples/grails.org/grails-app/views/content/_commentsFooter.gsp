<div id="btmSectionGraphicsWrapper">
    <%
        def commentHeader = "No Comments Yet"
        if (commentObject.comments.size() == 1) commentHeader = "1 Comment"
        if (commentObject.comments.size() > 1) commentHeader = "${commentObject.comments.size()} Comments"
    %>
	
    <div id="mountainLeft"></div>
    <div id="knight"></div>
    <div id="mountainRight"></div>
    <div id="castle"></div>
    <h2 class="commentsHeader"><a class='anchor' name='comments'><img src="${resource(dir:'images/new/plugins/icons', file:'comments.png')}" border="0" /> ${commentHeader}</a></h2>

</div><!-- btmSectionGraphicsWrapper-->				

<g:render template="../comments/comments" model="${[commentType:commentType, parentId:commentObject.id, comments:commentObject.comments]}"/>						
