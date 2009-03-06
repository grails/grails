<div class="shortComment">
    <g:set var='shortBody'>
        <g:if test="${comment.body.size() < 100}">${comment.body}</g:if>
        <g:else>${comment.body[0..100]}...</g:else>
    </g:set>
    <div class="body"><g:link controller='plugin' action='showComment' id="${comment.id}">${shortBody}</g:link></div>
    <div class="user">posted by ${comment.poster}</div>
</div>