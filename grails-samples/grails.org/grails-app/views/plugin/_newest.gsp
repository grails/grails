<h2>Newest Plugins</h2>
<ul id="newestPluginList">
    <g:each var='plugin' in="${plugins}">
        <li>
            <g:link controller="plugin" action="show" params="[name:plugin.name]">${plugin.title}</g:link> (<g:formatDate format="${grailsApplication.config.format.date}" date="${plugin.dateCreated}"/>) 
        </li>
    </g:each>
</ul>
