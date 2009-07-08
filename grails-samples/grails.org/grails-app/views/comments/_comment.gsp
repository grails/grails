<avatar:gravatar cssClass="commentAvatar" size="50"
        email="${comment?.poster.email}" gravatarRating="R"
        defaultGravatarUrl="${resource(absolute: true, dir:'/images',file:'grails-icon.png')}"/>
<div class="author">
    ${comment?.poster.login}
</div>
<div class="date">
    <g:formatDate format="MMM dd, yyyy HH:MM a" date="${comment.dateCreated}"/>
</div>

<div class='commentBody'>
    <wiki:text>
        ${comment?.body}
    </wiki:text>
</div>
