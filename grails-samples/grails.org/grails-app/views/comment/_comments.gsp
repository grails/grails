<div class="comments">
    <%
        def commentHeader = "No Comments Yet"
        if (comments.size == 1) commentHeader = "1 Comment"
        if (comments.size > 1) commentHeader = "${comments.size()} Comments"
    %>
    <h2><a class='anchor' name='comments'>${commentHeader}</a></h2>
    <ul id="commentList">
        <li></li>
        <g:each var="comment" in="${comments}">
            <li class="comment">
                <g:render template="/comment/comment" var='comment' bean="${comment}"/>
            </li>
        </g:each>
    </ul>

    <div id="nextComment" class="hidden"></div>

    <div id="postComment">
        <h2>Post a Comment</h2>
        <p class='tip'>You may use wiki text.</p>
        <g:if test="${!locked}">
            <g:if test="${jsec.principal()}">
                <script>
                    var handleComment = function() {
                        var lastComment = YAHOO.util.Dom.getLastChild('commentList');
                        var nextComment = YAHOO.util.Dom.get('nextComment');
                        var newComment = document.createElement('li');
                        newComment.id = 'newestComment';
                        newComment.className = 'comment';
                        YAHOO.util.Dom.setStyle(newComment, 'opacity', 0.0);
                        newComment.innerHTML = nextComment.innerHTML;
                        YAHOO.util.Dom.insertAfter(newComment, lastComment);
                        var attributes = {
                            opacity: { to: 1.0 }
                        };
                        var anim = new YAHOO.util.Anim('newestComment', attributes);
                        anim.animate();
                        newComment.id = null;
                        // clear comment textarea
                        YAHOO.util.Dom.get('comment').value = '';
                    };
                </script>
                <g:formRemote name='commentForm' url="[controller: commentType, action:'postComment', id:parentId]" update="nextComment" onComplete="handleComment()">
                    <textarea id='comment' name='comment' value=''></textarea>
                    <br/>
                    <g:submitButton name='submitComment' value='Post Comment'/>
                </g:formRemote>
            </g:if>
            <g:else>
                Login to leave a comment:
                <g:render template='../user/loginForm' var='originalURI' bean="${request.forwardURI}"/>
            </g:else>
        </g:if>
        <g:else>
            COMMENTS ARE LOCKED.
        </g:else>
    </div>

</div>
