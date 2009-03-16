
<%@ page import="org.grails.comments.CommentLink" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Admin: Comment List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'admin')}">Home</a></span>
        </div>
        <div class="body">
            <h1>Comments</h1>
            <p>Click on a comment ID below to edit or delete.</p
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />

                            <th>Poster</th>

                            <g:sortableColumn property="commentRef" title="Comment Ref"/>
                            
                   	        <g:sortableColumn property="type" title="Type" />
                        
                   	        <th>Comment</th>

                            <th>Date Created</th>

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${commentLinkInstanceList}" status="i" var="commentLinkInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="edit" id="${commentLinkInstance.comment.id}">${fieldValue(bean:commentLinkInstance, field:'id')}</g:link></td>

                            <td>${commentLinkInstance.comment.poster}</td>
                        
                            <td>${fieldValue(bean:commentLinkInstance, field:'commentRef')}</td>
                        
                            <td>${fieldValue(bean:commentLinkInstance, field:'type')}</td>
                        
                            <td><wiki:text>${commentLinkInstance.comment.body}</wiki:text></td>

                            <td>${commentLinkInstance.comment.dateCreated}</td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${commentLinkInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
