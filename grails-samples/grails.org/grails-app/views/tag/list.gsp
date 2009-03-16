
<%@ page import="org.grails.taggable.Tag" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Tag List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'admin')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Tag</g:link></span>
        </div>
        <div class="body">
            <h1>Tag List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="name" title="Name" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${tagInstanceList}" status="i" var="tagInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${tagInstance.id}">${fieldValue(bean:tagInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:tagInstance, field:'name')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${tagInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
