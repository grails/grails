<h2>Popular Tags</h2>
<ul id="popularTags">
    <g:each var='tag' in="${tags}">
        <li>
            <g:link controller="plugin" action="list" fragment="${(tag[0] + ' tags').encodeAsURL()}">${tag[0]}</g:link> (${tag[1]})
        </li>
    </g:each>
</ul>
