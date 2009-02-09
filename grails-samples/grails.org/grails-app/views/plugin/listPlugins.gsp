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
    <div class="list">
        <table>
            <thead>
            <tr>
                <g:sortableColumn property="id" title="Id"/>
                <g:sortableColumn property="title" title="Title"/>
                <g:sortableColumn property="body" title="Body"/>
                <g:sortableColumn property="description" title="Description"/>
                <g:sortableColumn property="installation" title="Installation"/>
                <g:sortableColumn property="faq" title="Faq"/>
            </tr>
            </thead>
            <tbody>
            <g:each in="${plugins}" status="i" var="plugin">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                    <td><g:link action="show" id="${plugin.id}">${fieldValue(bean: plugin, field: 'id')}</g:link></td>

                    <td>${fieldValue(bean: plugin, field: 'title')}</td>

                    <td>${fieldValue(bean: plugin, field: 'body')}</td>

                    <td>${fieldValue(bean: plugin, field: 'description')}</td>

                    <td>${fieldValue(bean: plugin, field: 'installation')}</td>

                    <td>${fieldValue(bean: plugin, field: 'faq')}</td>

                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
    <div class="paginateButtons">
        <g:paginate total="${totalPlugins}"/>
    </div>
</div>
</body>
</html>
