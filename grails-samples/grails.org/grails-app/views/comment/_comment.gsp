<a href="#comments">Top</a>
<div class='commentBody'>
    <wiki:text>
        ${comment?.body}
    </wiki:text>
</div>
<div class="commentAuthor"><a class='anchor' name="comment_${comment.id}">${comment?.user.login}</a></div>

<avatar:gravatar cssClass="commentAvatar" size="50"
        email="${comment?.user.email}" gravatarRating="R"
        defaultGravatarUrl="${createLinkTo(absolute: true, dir:'/images',file:'grails-icon.png')}"/>

<div class="commentDate">posted on ${comment.dateCreated}</div>