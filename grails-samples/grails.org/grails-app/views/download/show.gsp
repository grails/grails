
<%@ page import="org.grails.downloads.Download" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show Download</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Download List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Download</g:link></span>
        </div>
        <div class="body">
            <h1>Show Download</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                    
                        <tr class="prop">
                            <td valign="top" class="name">Id:</td>
                            
                            <td valign="top" class="value">${download.id}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Software Name:</td>
                            
                            <td valign="top" class="value">${download.softwareName}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Software Version:</td>
                            
                            <td valign="top" class="value">${download.softwareVersion}</td>
                            
                        </tr>

			             <tr class="prop">
	                            <td valign="top" class="name">Beta?:</td>

	                            <td valign="top" class="value">${download.betaRelease}</td>

	                    </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Count:</td>
                            
                            <td valign="top" class="value">${download.count}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Release Date:</td>
                            
                            <td valign="top" class="value">${download.releaseDate}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Release Notes:</td>
                            
                            <td valign="top" class="value">${download.releaseNotes}</td>
                            
                        </tr>

                       <tr class="prop">
                            <td valign="top" class="name">Files:</td>

                            <td valign="top" class="value">
                                <ul>
                                    <g:each var="file" in="${download.files?}">
                                        <li><g:link action="fileDetails" id="${file.id}">${file.title}</g:link></li>                                    
                                    </g:each>
                                </ul>
                                <br/>
                                <p>
                                    <g:link action="addFile" id="${download.id}">Add File</g:link>
                                </p>
                            </td>

                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${download?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
