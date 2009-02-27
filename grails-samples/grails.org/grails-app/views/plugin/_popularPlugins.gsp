<h2>Popular Plugins</h2>
<table id="popularPluginList">
    <g:each var='plugin' in="${plugins}">
        <tr>
            <td>
                <g:link controller="plugin" action="show" params="[name:plugin[0].name]">${plugin[0].title}</g:link>
            </td>
            <td>
                <g:render template="ratingDisplay" var="average" bean="${plugin[1]}"/>
            </td>
            <td>
                (${plugin[2]} votes)
            </td>
        </tr>
    </g:each>
</table>
