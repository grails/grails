<h2>Popular Plugins</h2>
<ul id="popularPluginList">
    <g:each var='plugin' in="${plugins}">
        <li>
            <g:link controller="plugin" action="show" params="[name:plugin[0].name]">${plugin[0].title}</g:link> ${plugin[1]} (${plugin[2]} ratings)
        </li>
    </g:each>
</ul>
