
<%@ page import="org.grails.plugin.Plugin" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Plugin</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Plugin List</g:link></span>
        </div>
        <div class="body">
            <h1>Create Plugin</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${pluginInstance}">
            <div class="errors">
                <g:renderErrors bean="${pluginInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name">Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'name','errors')}">
                                    <input type="text" id="name" name="name" value="${fieldValue(bean:pluginInstance,field:'name')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="description">Description:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'description','errors')}">
                                    <g:select optionKey="id" from="${org.grails.wiki.WikiPage.list()}" name="description.id" value="${pluginInstance?.description?.id}" noSelection="['null':'']"></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="installation">Installation:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'installation','errors')}">
                                    <g:select optionKey="id" from="${org.grails.wiki.WikiPage.list()}" name="installation.id" value="${pluginInstance?.installation?.id}" noSelection="['null':'']"></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="faq">Faq:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'faq','errors')}">
                                    <g:select optionKey="id" from="${org.grails.wiki.WikiPage.list()}" name="faq.id" value="${pluginInstance?.faq?.id}" noSelection="['null':'']"></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="screenshots">Screenshots:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'screenshots','errors')}">
                                    <g:select optionKey="id" from="${org.grails.wiki.WikiPage.list()}" name="screenshots.id" value="${pluginInstance?.screenshots?.id}" noSelection="['null':'']"></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="author">Author:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'author','errors')}">
                                    <input type="text" id="author" name="author" value="${fieldValue(bean:pluginInstance,field:'author')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="grailsVersion">Grails Version:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'grailsVersion','errors')}">
                                    <input type="text" id="grailsVersion" name="grailsVersion" value="${fieldValue(bean:pluginInstance,field:'grailsVersion')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lastReleased">Last Released:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'lastReleased','errors')}">
                                    <g:datePicker name="lastReleased" value="${pluginInstance?.lastReleased}" precision="minute" noSelection="['':'']"></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="currentRelease">Current Release:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'currentRelease','errors')}">
                                    <input type="text" id="currentRelease" name="currentRelease" value="${fieldValue(bean:pluginInstance,field:'currentRelease')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="authorEmail">Author Email:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'authorEmail','errors')}">
                                    <input type="text" id="authorEmail" name="authorEmail" value="${fieldValue(bean:pluginInstance,field:'authorEmail')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="dateCreated">Date Created:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'dateCreated','errors')}">
                                    <g:datePicker name="dateCreated" value="${pluginInstance?.dateCreated}" precision="minute" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="documentationUrl">Documentation Url:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'documentationUrl','errors')}">
                                    <input type="text" id="documentationUrl" name="documentationUrl" value="${fieldValue(bean:pluginInstance,field:'documentationUrl')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="downloadUrl">Download Url:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'downloadUrl','errors')}">
                                    <input type="text" id="downloadUrl" name="downloadUrl" value="${fieldValue(bean:pluginInstance,field:'downloadUrl')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="featured">Featured:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'featured','errors')}">
                                    <g:checkBox name="featured" value="${pluginInstance?.featured}" ></g:checkBox>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lastUpdated">Last Updated:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'lastUpdated','errors')}">
                                    <g:datePicker name="lastUpdated" value="${pluginInstance?.lastUpdated}" precision="minute" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="official">Official:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'official','errors')}">
                                    <g:checkBox name="official" value="${pluginInstance?.official}" ></g:checkBox>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="title">Title:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'title','errors')}">
                                    <input type="text" id="title" name="title" value="${fieldValue(bean:pluginInstance,field:'title')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="avgRating">Avg Rating:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'avgRating','errors')}">
                                    <input type="text" id="avgRating" name="avgRating" value="${fieldValue(bean:pluginInstance,field:'avgRating')}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="cacheService">Cache Service:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'cacheService','errors')}">
                                    
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="fisheye">Fisheye:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'fisheye','errors')}">
                                    
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="onAddComment">On Add Comment:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'onAddComment','errors')}">
                                    
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="pluginService">Plugin Service:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'pluginService','errors')}">
                                    
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
