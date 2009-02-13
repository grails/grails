<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Grails Plugins</title>
    <meta content="subpage" name="layout"/>
</head>
<body>
<div id="contentPane">
    <div id="infoLinks" style="margin-left:520px;">
        <g:link controller="plugin" action="createPlugin">Create Plugin Page</g:link>
    </div>

    <h1>Plugin List</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <ul>
        <g:each var="plugin" in="${plugins}">
            <li>
                <g:if test="${!plugin.name}">
                    ${plugin.title} (there is no name associated with this plugin)
                </g:if>
                <g:else>
                    <g:link action="show" params="${[name:plugin.name]}">${plugin.title}</g:link>
                </g:else>
            </li>
        </g:each>
    </ul>
</div>
</body>
</html>
