<h2>Popular Plugins</h2>
<ul id="popularPluginList">
    <g:each var='plugin' in="${plugins}">
        <li>
            <g:link controller="plugin" action="show" params="[name:plugin.name]">${plugin.title}</g:link>
            <rateable:ratings bean="${plugin}" active='false'/>
        </li>
    </g:each>
</ul>
