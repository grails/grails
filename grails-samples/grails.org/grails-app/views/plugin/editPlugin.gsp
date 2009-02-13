<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Edit ${plugin.title} Plugin</title>
    <meta content="subpage" name="layout"/>
</head>
<body>
<div id="contentPane">
    <div id="infoLinks" style="margin-left:520px;">
        <g:link class="home" controller="plugin" action="list">Plugin List</g:link>
    </div>

    <h1>Edit Plugin</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${plugin}">
        <div class="errors">
            <g:renderErrors bean="${plugin}" as="list"/>
        </div>
    </g:hasErrors>
    
    <g:form action='edit' id="${plugin?.id}">
        <input type="hidden" name="id" value="${pluginInstance?.id}" />
                <input type="hidden" name="version" value="${pluginInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

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
                                    <input type="text" id="description" name="description" value="${fieldValue(bean:pluginInstance,field:'description')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="faq">Faq:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'faq','errors')}">
                                    <input type="text" id="faq" name="faq" value="${fieldValue(bean:pluginInstance,field:'faq')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="screenshots">Screenshots:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'screenshots','errors')}">
                                    <input type="text" id="screenshots" name="screenshots" value="${fieldValue(bean:pluginInstance,field:'screenshots')}"/>
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
                                    <label for="currentRelease">Current Release:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'currentRelease','errors')}">
                                    <input type="text" id="currentRelease" name="currentRelease" value="${fieldValue(bean:pluginInstance,field:'currentRelease')}"/>
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
                                    <label for="official">Official:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'official','errors')}">
                                    <g:checkBox name="official" value="${pluginInstance?.official}" ></g:checkBox>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="tags">Tags:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:pluginInstance,field:'tags','errors')}">
                                    <g:select name="tags"
from="${org.grails.plugin.Tag.list()}"
size="5" multiple="yes" optionKey="id"
value="${pluginInstance?.tags}" />

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
