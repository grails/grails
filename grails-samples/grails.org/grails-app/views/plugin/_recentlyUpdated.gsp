<h2>Recently Updated</h2>
<ul id="recentlyUpdatedPluginList">
    <g:each var='plugin' in="${plugins}">
        <li>
            <g:link controller="plugin" action="show" params="[name:plugin.name]">${plugin.title}</g:link> ${plugin.currentRelease} <nobr>(<g:formatDate format="${grailsApplication.config.format.date}" date="${plugin.lastUpdated}"/>)</nobr>
        </li>
    </g:each>
</ul>
