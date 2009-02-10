<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Grails Plugins</title>
    <meta content="subpage" name="layout"/>
</head>
<body>
<div id="contentPane">
    <div id="infoLinks" style="margin-left:520px;">
        <g:link class="create" controller="plugin" action="create">Create Plugin</g:link>
    </div>

    <h1>Plugin List</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <ul>
        <g:each var="plugin" in="${plugins}">
            <li><g:link action="show" params="${[title:plugin.title]}">${plugin.title}</g:link></li>
        </g:each>
    </ul>
</div>
</body>
</html>
