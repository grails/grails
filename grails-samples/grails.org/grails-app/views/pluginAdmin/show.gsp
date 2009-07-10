
<%@ page import="org.grails.plugin.Plugin" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show Plugin</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Plugin List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Plugin</g:link></span>
        </div>
        <div class="body">
            <h1>Show Plugin</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                    
                        <tr class="prop">
                            <td valign="top" class="name">Id:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'id')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Name:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'name')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Description:</td>
                            
                            <td valign="top" class="value"><g:link controller="wikiPage" action="show" id="${pluginInstance?.description?.id}">${pluginInstance?.description?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Installation:</td>
                            
                            <td valign="top" class="value"><g:link controller="wikiPage" action="show" id="${pluginInstance?.installation?.id}">${pluginInstance?.installation?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Faq:</td>
                            
                            <td valign="top" class="value"><g:link controller="wikiPage" action="show" id="${pluginInstance?.faq?.id}">${pluginInstance?.faq?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Screenshots:</td>
                            
                            <td valign="top" class="value"><g:link controller="wikiPage" action="show" id="${pluginInstance?.screenshots?.id}">${pluginInstance?.screenshots?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Author:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'author')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Grails Version:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'grailsVersion')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Last Released:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'lastReleased')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Current Release:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'currentRelease')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Author Email:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'authorEmail')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Date Created:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'dateCreated')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Documentation Url:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'documentationUrl')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Download Url:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'downloadUrl')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Featured:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'featured')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Last Updated:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'lastUpdated')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Official:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'official')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Title:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'title')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Avg Rating:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'avgRating')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Cache Service:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'cacheService')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Fisheye:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'fisheye')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">On Add Comment:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'onAddComment')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Plugin Service:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:pluginInstance, field:'pluginService')}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${pluginInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
