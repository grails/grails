
<%@ page import="org.grails.downloads.Download" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Download</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Download List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Download</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Download</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${download}">
            <div class="errors">
                <g:renderErrors bean="${download}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${download?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="softwareName">Software Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:download,field:'softwareName','errors')}">
                                    <input type="text" id="softwareName" name="softwareName" value="${fieldValue(bean:download,field:'softwareName')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="softwareVersion">Software Version:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:download,field:'softwareVersion','errors')}">
                                    <input type="text" id="softwareVersion" name="softwareVersion" value="${fieldValue(bean:download,field:'softwareVersion')}"/>
                                </td>
                            </tr> 
                        
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="releaseDate">Release Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:download,field:'releaseDate','errors')}">
                                    <g:datePicker name="releaseDate" value="${download?.releaseDate}" ></g:datePicker>
                                </td>
                            </tr> 

		                    <tr class="prop">
	                                <td valign="top" class="name">
	                                    <label for="releaseDate">Beta Release?:</label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean:download,field:'betaRelease','errors')}">
	                                    <g:checkBox name="betaRelease" value="${download?.betaRelease}" ></g:checkBox>
	                                </td>
	                            </tr> 

       
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
