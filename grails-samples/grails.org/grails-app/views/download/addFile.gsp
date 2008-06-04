
<%@ page import="org.grails.downloads.Download" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Add File</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Download List</g:link></span>
        </div>
        <div class="body">
            <h1>Add File to ${download.softwareName} - ${download.softwareVersion}</h1>
            <g:if test="${message}">
                <div class="message">${message}</div>
            </g:if>
            <g:hasErrors bean="${downloadFile}">
            <div class="errors">
                <g:renderErrors bean="${downloadFile}" as="list" />
            </div>
            </g:hasErrors>
            <g:form url="[action:'addFile',id:download.id]" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="title">Download Type (zip or tar/gz etc.):</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:downloadFile,field:'title','errors')}">
                                    <g:textField name="title" value="${downloadFile?.title}" />
                                </td>
                            </tr>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name">Mirror Name:</label>
                                </td>
                                <td valign="top">
                                    <g:textField name="name" value="${url}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="url">File URL:</label>
                                </td>
                                <td valign="top">
                                    <g:textField name="url" value="${url}" />
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
