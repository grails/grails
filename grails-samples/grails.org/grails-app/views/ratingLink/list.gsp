
<%@ page import="org.grails.rateable.RatingLink" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Admin: Rating List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'admin')}">Home</a></span>
        </div>
        <div class="body">
            <h1>RatingLink List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <td>Plugin</td>

                   	        <th>Rating</th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${ratingLinks}" status="i" var="ratingLink">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="edit" id="${ratingLink.rating.id}">${fieldValue(bean:ratingLink, field:'id')}</g:link></td>
                        
                            <td><g:link controller='plugin' action='show' params="${[name:ratingLink.plugin.name]}">${ratingLink.plugin.title}</g:link></td>
                        
                            <td>${fieldValue(bean:ratingLink, field:'rating')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${total}" />
            </div>
        </div>
    </body>
</html>
