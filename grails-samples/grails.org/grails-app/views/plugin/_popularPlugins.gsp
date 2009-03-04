<h2>Popular Plugins</h2>
<ul id="popularPluginList">
    <g:each var='plugin' in="${plugins}">
        <li>
            <g:link controller="plugin" action="show" params="[name:plugin[0].name]">${plugin[0].title}</g:link>
            <g:render template="ratingDisplay" model="[average:plugin[1], votes:plugin[2]]"/>
        </li>
    </g:each>
</ul>
