
<%@ page import="org.grails.plugin.Plugin" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Plugin</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Plugin List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Plugin</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Plugin</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${pluginInstance}">
            <div class="errors">
                <g:renderErrors bean="${pluginInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${pluginInstance?.id}" />
                <input type="hidden" name="version" value="${pluginInstance?.version}" />
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
                                    <label for="authorEmail">Author Email:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'authorEmail','errors')}">
                                    <input type="text" id="authorEmail" name="authorEmail" value="${fieldValue(bean:pluginInstance,field:'authorEmail')}"/>
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
                                    <label for="official">Official:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'official','errors')}">
                                    <g:checkBox name="official" value="${pluginInstance?.official}" ></g:checkBox>
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
