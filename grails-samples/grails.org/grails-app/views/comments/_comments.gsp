<div class="comments">
    <cache:text id="comments_for_${commentType}_${parentId}">
        <ul id="commentList">
            <li></li>
            <g:each var="comment" status='i' in="${comments}">
                <g:set var='oddEven'>
                    <g:if test="${i % 2 == 0}">even</g:if>
                    <g:else>odd</g:else>
                </g:set>
                <li class="comment ${oddEven}">
                    <g:render template="/comments/comment" var='comment' bean="${comment}"/>
                </li>
            </g:each>
        </ul>
    </cache:text>

    <div id="nextComment" class="hidden"></div>

    <div id="postComment">

        <h2 id='postBoxTrigger'>Post a Comment</h2>

        <div id='postBox' class="hidden">

            <g:if test="${!locked}">
                <g:if test="${jsec.principal()}">
                    <script>
                        var handleComment = function() {
                            var lastComment = YAHOO.util.Dom.getLastChild('commentList');
                            var oddEven = YAHOO.util.Dom.hasClass(lastComment, 'odd') ? 'even' : 'odd';
                            var nextComment = YAHOO.util.Dom.get('nextComment');
                            var newComment = document.createElement('li');
                            newComment.id = 'newestComment';
                            newComment.className = 'comment ' + oddEven;
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
                    <p class='tip'>You may use wiki text. <g:render template='/common/wikiSyntaxLink'/></p>
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

        <script>
            YAHOO.util.Event.onDOMReady(function() {
                YAHOO.util.Event.on('postBoxTrigger', 'click', function() {
                    var postBox = YAHOO.util.Dom.get('postBox');
                    YAHOO.util.Dom.setStyle(postBox, 'opacity', 0.0);
                    YAHOO.util.Dom.removeClass(postBox, 'hidden');
                    var attributes = {
                        opacity: { to: 1.0 }
                    };
                    var anim = new YAHOO.util.Anim('postBox', attributes);
                    anim.animate();
                    YAHOO.util.Event.removeListener('postBoxTrigger');
                });
            });
        </script>
    </div>

</div>
