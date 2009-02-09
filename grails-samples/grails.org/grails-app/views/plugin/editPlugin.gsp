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
        <input type="hidden" name="id" value="${plugin?.id}"/>
        <input type="hidden" name="version" value="${plugin?.version}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="title">Title:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: plugin, field: 'title', 'errors')}">
                        <input type="text" id="title" name="title" value="${fieldValue(bean: plugin, field: 'title')}"/>
                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="body">Body:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: plugin, field: 'body', 'errors')}">
                        <input type="text" id="body" name="body" value="${fieldValue(bean: plugin, field: 'body')}"/>
                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="description">Description:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: plugin, field: 'description', 'errors')}">
                        <input type="text" id="description" name="description" value="${fieldValue(bean: plugin, field: 'description')}"/>
                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="installation">Installation:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: plugin, field: 'installation', 'errors')}">
                        <input type="text" id="installation" name="installation" value="${fieldValue(bean: plugin, field: 'installation')}"/>
                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="faq">Faq:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: plugin, field: 'faq', 'errors')}">
                        <input type="text" id="faq" name="faq" value="${fieldValue(bean: plugin, field: 'faq')}"/>
                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="screenshots">Screenshots:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: plugin, field: 'screenshots', 'errors')}">
                        <input type="text" id="screenshots" name="screenshots" value="${fieldValue(bean: plugin, field: 'screenshots')}"/>
                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="author">Author:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: plugin, field: 'author', 'errors')}">
                        <input type="text" id="author" name="author" value="${fieldValue(bean: plugin, field: 'author')}"/>
                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="authorEmail">Author Email:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: plugin, field: 'authorEmail', 'errors')}">
                        <input type="text" id="authorEmail" name="authorEmail" value="${fieldValue(bean: plugin, field: 'authorEmail')}"/>
                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="official">Official:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: plugin, field: 'official', 'errors')}">
                        <g:checkBox name="official" value="${plugin?.official}"></g:checkBox>
                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="tags">Tags:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: plugin, field: 'tags', 'errors')}">
                        <g:select name="tags"
                                from="${org.grails.plugin.Tag.list()}"
                                size="5" multiple="yes" optionKey="id"
                                value="${plugin?.tags}"/>

                    </td>
                </tr>

                </tbody>
            </table>
        </div>
        <div class="buttons">
            <g:submitButton name="save" value="Update" />
            <g:submitButton name="delete" onclick="return confirm('Are you sure?');" value="Delete"/>
        </div>
    </g:form>
</div>
</body>
</html>
