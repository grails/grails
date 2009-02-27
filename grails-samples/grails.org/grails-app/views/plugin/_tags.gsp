<g:each var='tag' in="${plugin.tags.sort {it.name}}">
    <span class="tag"><g:link action="list" fragment="${(tag.name + ' tags').encodeAsURL()}">${tag.name}</g:link>
        <jsec:isLoggedIn>
            <img onclick="new Ajax.Updater('pluginTags','${createLink(controller:'plugin',action:'removeTag',id:plugin.id,params:[tagName:tag.name])}',{asynchronous:true,evalScripts:true,method:'POST'});" 
                src="${createLinkTo(dir: 'images/famfamfam', file: 'delete.png')}"/>
        </jsec:isLoggedIn>
    </span>
</g:each>