
<%@ page import="org.grails.downloads.Download" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Download List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Download</g:link></span>
        </div>
        <div class="body">
            <h1>Download List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="softwareName" title="Software Name" />
                        
                   	        <g:sortableColumn property="softwareVersion" title="Software Version" />
                        
                   	        <g:sortableColumn property="count" title="Count" />
                        
                   	        <g:sortableColumn property="releaseDate" title="Release Date" />
                        
                   	        <g:sortableColumn property="releaseNotes" title="Release Notes" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${downloadList}" status="i" var="download">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${download.id}">${download.id?.encodeAsHTML()}</g:link></td>
                        
                            <td>${download.softwareName?.encodeAsHTML()}</td>
                        
                            <td>${download.softwareVersion?.encodeAsHTML()}</td>
                        
                            <td>${download.count?.encodeAsHTML()}</td>
                        
                            <td>${download.releaseDate?.encodeAsHTML()}</td>
                        
                            <td>${download.releaseNotes?.encodeAsHTML()}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${Download.count()}" />
            </div>
        </div>
    </body>
</html>
