<table>
    <tbody>
    <tr>
        <td class="body" colspan='2'>
            <div class='permalink'><a href="#comment_${comment.id}" name="comment_${comment.id}">link</a></div>
            <avatar:gravatar cssClass="commentAvatar" size="50"
                    email="${comment?.user.email}" gravatarRating="R"
                    defaultGravatarUrl="${createLinkTo(absolute: true, dir:'/images',file:'grails-icon.png')}"/>
            <div class='commentBody'>
                <wiki:text>
                    ${comment?.body}
                </wiki:text>
            </div>
        </td>

    </tr>
    <tr>
        <td class="date">
            <g:formatDate format="MMM dd, yyyy HH:MM a" date="${comment.dateCreated}"/>
        </td>
        <td class="author">
            ${comment?.user.login}
        </td>

    </tr>
    </tbody>
</table>
