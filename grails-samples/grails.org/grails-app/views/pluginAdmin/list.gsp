
<%@ page import="org.grails.plugin.Plugin" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Plugin List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Plugin</g:link></span>
        </div>
        <div class="body">
            <h1>Plugin List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="name" title="Name" />
                        
                   	        <th>Description</th>
                   	    
                   	        <th>Installation</th>
                   	    
                   	        <th>Faq</th>
                   	    
                   	        <th>Screenshots</th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${pluginInstanceList}" status="i" var="pluginInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${pluginInstance.id}">${fieldValue(bean:pluginInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:pluginInstance, field:'name')}</td>
                        
                            <td>${fieldValue(bean:pluginInstance, field:'description')}</td>
                        
                            <td>${fieldValue(bean:pluginInstance, field:'installation')}</td>
                        
                            <td>${fieldValue(bean:pluginInstance, field:'faq')}</td>
                        
                            <td>${fieldValue(bean:pluginInstance, field:'screenshots')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${pluginInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
