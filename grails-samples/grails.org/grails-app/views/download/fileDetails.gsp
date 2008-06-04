
<%@ page import="org.grails.downloads.Download" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>File Details</title>
        <g:javascript library="prototype" />
        <style type="text/css">            
            .textField {
                position:absolute;
                left:100px;
            }
            .spaced  {
                margin-bottom:10px;
            }
        </style>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Download List</g:link></span>
        </div>
        <div class="body">
            <h1>File Mirrors: ${downloadFile.title}</h1>
            <br/>
            <div id="mirrors">
                <g:render template="mirrorList" model="${pageScope.variables}" />
            </div>
            <br/>
            <h3 class="spaced">Add Mirror</h3>
            <g:formRemote name="mirrorForm" url="[action:'addMirror', id:downloadFile.id]" update="mirrors">
                <div class="spaced">
                    <label for="name">Name:</label>
                    <g:textField name="name" class="textField"></g:textField>
                </div>
                <div class="spaced">
                    <label for="url" >URL:</label>
                    <g:textField name="url" class="textField"></g:textField>
                </div>
                <g:submitButton name="addMirror" value="Add Mirror" />
            </g:formRemote>
        </div>
    </body>
</html>
