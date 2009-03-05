
<g:each var='tag' in="${plugin.tags.sort()}">
    <span class="tag"><g:link action="list" fragment="${(tag + ' tags').encodeAsURL()}">${tag}</g:link>
    <g:if test="${!disabled}">
        %{-- If logged in, we're going to attach the normal ajax click listener --}%
        <jsec:isLoggedIn>
            <g:set var='clickHandler'>
                onclick="new Ajax.Updater('pluginTags','${createLink(controller:'plugin',action:'removeTag',id:plugin.id,params:[tagName:tag])}',{asynchronous:true,evalScripts:true,method:'POST'});"
            </g:set>
        </jsec:isLoggedIn>
        <img ${clickHandler} id="remove_${tag}_tag_from_${plugin.id}" src="${createLinkTo(dir: 'images/famfamfam', file: 'delete.png')}"/>
        %{-- If not logged in, we'll add a custom listener that will defer to login form --}%
        <jsec:isNotLoggedIn>
            <script>
                YAHOO.util.Event.onDOMReady(function() {
                    // on show, put the dialog in the right place
                    YAHOO.util.Event.on("remove_${tag}_tag_from_${plugin.id}", 'click', function() {
                        window.location = "${createLink(controller:'user', action:'login', params:[originalURI:request.forwardURI])}";
                    });
                });
            </script>
        </jsec:isNotLoggedIn>
    </g:if>
    </span>
</g:each>