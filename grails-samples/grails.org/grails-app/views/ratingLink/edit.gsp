
<%@ page import="org.grails.rateable.Rating" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Admin: Edit Rating</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'admin')}">Home</a></span>
            <span class="menuButton"><g:link controller="ratingLink" class="list" action="list">Rating List</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Rating</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${ratingInstance}">
            <div class="errors">
                <g:renderErrors bean="${ratingInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${ratingInstance?.id}" />
                <input type="hidden" name="version" value="${ratingInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="raterClass">Rater Class:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:ratingInstance,field:'raterClass','errors')}">
                                    <input type="text" id="raterClass" name="raterClass" value="${fieldValue(bean:ratingInstance,field:'raterClass')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="raterId">Rater Id:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:ratingInstance,field:'raterId','errors')}">
                                    <input type="text" id="raterId" name="raterId" value="${fieldValue(bean:ratingInstance,field:'raterId')}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="dateCreated">Date Created:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:ratingInstance,field:'dateCreated','errors')}">
                                    <g:datePicker name="dateCreated" value="${ratingInstance?.dateCreated}" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lastUpdated">Last Updated:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:ratingInstance,field:'lastUpdated','errors')}">
                                    <g:datePicker name="lastUpdated" value="${ratingInstance?.lastUpdated}" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="stars">Stars:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:ratingInstance,field:'stars','errors')}">
                                    <input type="text" id="stars" name="stars" value="${fieldValue(bean:ratingInstance,field:'stars')}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="rater">Rater:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:ratingInstance,field:'rater','errors')}">
                                    
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
