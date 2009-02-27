<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Grails Plugins</title>
    <meta content="subpage" name="layout"/>
</head>
<body>
<div id="contentPane">
    <div id="infoLinks" style="margin-left:520px;">
        <g:link class="home" controller="plugin" action="index">Plugins Home</g:link><br/>
        <g:link controller="plugin" action="createPlugin">Create Plugin Page</g:link>
    </div>

    <h1>Plugin List</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    
    <g:each var='tagMap' in="${pluginMap}">
        <a id="${(tagMap.key + ' tags').encodeAsURL()}"><h2>${tagMap.key}</h2></a>
        <ul>
            <g:each var='plugin' in="${tagMap.value}">
                <li>
                    <g:if test="${plugin.name.startsWith('fix-this-')}">
                        <g:link action="show" params="${[name:plugin.name]}">${plugin.title}</g:link> (RENAME ME!)
                    </g:if>
                    <g:else>
                        <g:link action="show" params="${[name:plugin.name]}">${plugin.title}</g:link>
                    </g:else>
                </li>
            </g:each>
        </ul>
    </g:each>

</div>
</body>
</html>
