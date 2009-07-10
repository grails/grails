<%
def transform = { list, bucketSize ->
   def total = list.size()
   def nbBuckets = total.intdiv(bucketSize)
   def remainder = total%bucketSize
   def newList = (0..<nbBuckets).collect { [*list[(it*bucketSize)..<((it+1)*bucketSize)]] }
   newList << list[(total-1)-remainder..-1]
}		
commentsTotal = comments.size()		
comments = transform(comments, 5)
%>

<div class="comments">

        <div id="commentList">
            
            <g:each var="commentGroup" status='i' in="${comments}">
                <div id="commentsGroup${i+1}" class="commentsBox ${ i == 0 ? 'commentShown' : 'commentHidden'}">
					<g:each var="comment" in="${commentGroup}">
						<div class="comment">
	                    	<g:render template="/comments/comment" var='comment' bean="${comment}"/>						
						</div>
					
					</g:each>

                </div>
            </g:each>
        </div>
		<div id="commentPaginator" class="yui-skin-sam">
			
		</div>

		<script type="text/javascript" charset="utf-8">
			YAHOO.util.Event.onDOMReady(function () {

				// Set up the application under the YAHOO.example namespace
				var Ex = YAHOO.namespace('paginator');

				Ex.content = YAHOO.util.Dom.get('commentList');
				
				Ex.handlePagination = function (state) {
				    // Show the appropriate content for the requested page
					var toShow = 'commentsGroup'+state.page
					var el = $(toShow)
					el.style.display = 'block'
					var rest = document.getElementsByClassName("commentsBox")

					for(var i = 0; i < rest.length; i++) {
						if(rest[i].id!=toShow) {							
							rest[i].style.display='none';
						}
					}
				    // Update the Paginator's state, confirming change
				    Ex.paginator.setState(state);
				};
	


				Ex.paginator = new YAHOO.widget.Paginator({
				    rowsPerPage : 5, // one div per page
				    totalRecords : ${commentsTotal},
				    containers : 'commentPaginator' // controls will be rendered into this element
				});
				Ex.paginator.subscribe('changeRequest', Ex.handlePagination); 
				Ex.paginator.render(); 
			});
		</script>

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
