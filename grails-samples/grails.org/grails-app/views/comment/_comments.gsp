<div class="comments">
    <h2><a class='anchor' name='comments'>Comments</a></h2>
    <g:each var="comment" in="${comments}">

        <div class="comment">
            <wiki:text>
                ${comment?.body}
            </wiki:text>
            <div class="author"><a class='anchor' name="comment_${comment.id}">${comment?.user.login}</a></div>

            <avatar:gravatar cssClass="gravatar" size="50"
                    email="${comment?.user.email}" gravatarRating="R"
                    defaultGravatarUrl="${createLinkTo(absolute: true, dir:'/images',file:'grails-icon.png')}"
            />

            <div class="date">posted on ${comment.dateCreated}</div>
        </div>
    </g:each>

    <div class="postComment">
        <h2>Post a Comment</h2>
        <g:if test="${!locked}">
            <g:if test="${jsec.principal()}">
                <g:form name='commentForm' action='postComment' id='${parentId}'>
                    <textarea id='comment' name='comment' value=''></textarea>
                    <br/>
                    <g:submitButton name='submitComment' value='Post Comment'/>
                </g:form>
            </g:if>
            <g:else>
                <g:link action='postComment' id='${parentId}'>Login</g:link> to leave a comment
            </g:else>
        </g:if>
        <g:else>
            COMMENTS ARE LOCKED.
        </g:else>
    </div>

</div>
