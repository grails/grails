
<%@ page import="org.grails.blog.BlogEntry" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit BlogEntry</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">BlogEntry List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New BlogEntry</g:link></span>
        </div>
        <div class="body">
            <h1>Edit BlogEntry</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${blogEntryInstance}">
            <div class="errors">
                <g:renderErrors bean="${blogEntryInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${blogEntryInstance?.id}" />
                <input type="hidden" name="version" value="${blogEntryInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="title">Title:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:blogEntryInstance,field:'title','errors')}">
                                    <input type="text" id="title" name="title" value="${fieldValue(bean:blogEntryInstance,field:'title')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="body">Body:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:blogEntryInstance,field:'body','errors')}">
                                    <input type="text" id="body" name="body" value="${fieldValue(bean:blogEntryInstance,field:'body')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="author">Author:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:blogEntryInstance,field:'author','errors')}">
                                    <input type="text" id="author" name="author" value="${fieldValue(bean:blogEntryInstance,field:'author')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="dateCreated">Date Created:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:blogEntryInstance,field:'dateCreated','errors')}">
                                    <g:datePicker name="dateCreated" value="${blogEntryInstance?.dateCreated}" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lastUpdated">Last Updated:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:blogEntryInstance,field:'lastUpdated','errors')}">
                                    <g:datePicker name="lastUpdated" value="${blogEntryInstance?.lastUpdated}" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="locked">Locked:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:blogEntryInstance,field:'locked','errors')}">
                                    <g:checkBox name="locked" value="${blogEntryInstance?.locked}" ></g:checkBox>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="published">Published:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:blogEntryInstance,field:'published','errors')}">
                                    <g:checkBox name="published" value="${blogEntryInstance?.published}" ></g:checkBox>
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
