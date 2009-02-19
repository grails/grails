<g:each var='tag' in="${plugin.tags}">
    <span class="tag"><a href="${createLink(action: 'list')}#${tag.name} tags">${tag.name}</a><img src="${createLinkTo(dir: 'images/famfamfam', file: 'delete.png')}"/></span>
</g:each>