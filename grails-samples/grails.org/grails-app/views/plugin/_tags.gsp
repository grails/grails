<g:each var='tag' in="${plugin.tags.sort {it.name}}">
    <span class="tag"><a href="${createLink(action: 'list')}#${tag.name} tags">${tag.name}</a>
        <jsec:isLoggedIn>
            <img onclick="new Ajax.Updater('pluginTags','${createLink(controller:'plugin',action:'removeTag',id:plugin.id,params:[tagName:tag.name])}',{asynchronous:true,evalScripts:true,method:'POST'});" 
                src="${createLinkTo(dir: 'images/famfamfam', file: 'delete.png')}"/>
        </jsec:isLoggedIn>
    </span>
</g:each>