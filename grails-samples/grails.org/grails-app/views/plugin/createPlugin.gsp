<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>New Plugin</title>
    <meta content="subpage" name="layout"/>
</head>
<body>
<div id="contentPane">
    <div id="infoLinks" style="margin-left:520px;">
        <g:link controller="plugin" action="list">All Plugins</g:link><br/>
    </div>
    <h1>Create Plugin</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${pluginInstance}">
        <div class="errors">
            <g:renderErrors bean="${pluginInstance}" as="list"/>
        </div>
    </g:hasErrors>
    <g:form name="createPlugin" url="[controller:'plugin', action:'createPlugin']">
        <div class="dialog">
            <table>
                <tbody>

                <plugin:input
                        name="Name"
                        description="This is what you would type in the command line after 'grails install-plugin'.">
                    <input type="text" id="name" name="name" value="${fieldValue(bean: pluginInstance, field: 'name')}"/>
                </plugin:input>


                <plugin:input
                        name="Title"
                        description="The 'Human-Readable' title of your plugin">
                    <input type="text" id="title" name="title" value="${fieldValue(bean: pluginInstance, field: 'title')}"/>
                </plugin:input>

                <plugin:input
                        name="Description"
                        description="The long description of your plugin.">
                    <textarea cols="60" rows="14" id="description" name="description" value="${fieldValue(bean: pluginInstance, field: 'description')}"></textarea>
                </plugin:input>

                <plugin:input
                        name="FAQ"
                        description="Frequently asked questions (if you leave this blank, it will not be included in your page).">
                    <textarea cols="60" rows="14" id="faq" name="faq" value="${fieldValue(bean: pluginInstance, field: 'faq')}"></textarea>
                </plugin:input>

                <plugin:input
                        name="Screenshots"
                        description="Attach any screenshots here (if you leave this blank, it will not be included in your page).">
                    <textarea cols="60" rows="14" type="text" id="screenshots" name="screenshots" value="${fieldValue(bean: pluginInstance, field: 'screenshots')}"></textarea>
                </plugin:input>

                <plugin:input
                        name="Author"
                        description="Plugin author's name(s)">
                    <input type="text" id="author" name="author" value="${fieldValue(bean: pluginInstance, field: 'author')}"/>
                </plugin:input>

                <plugin:input
                        name="Author Email"
                        description="Plugin author email addresses">
                    <input type="text" id="authorEmail" name="authorEmail" value="${fieldValue(bean: pluginInstance, field: 'authorEmail')}"/>
                </plugin:input>

                <plugin:input
                        name="Grails Version"
                        description="The Grails version this plugin was developed under.">
                    <input type="text" id="grailsVersion" name="grailsVersion" value="${fieldValue(bean: pluginInstance, field: 'grailsVersion')}"/>
                </plugin:input>

                <plugin:input
                        name="Current Release"
                        description="Current plugin release">
                    <input type="text" id="currentRelease" name="currentRelease" value="${fieldValue(bean: pluginInstance, field: 'currentRelease')}"/>
                </plugin:input>

                <plugin:input
                        name="Documentation URL"
                        description="Where on the web is are your docs?  If this is it, then leave blank.">
                    <input type="text" id="documentationUrl" name="documentationUrl" value="${fieldValue(bean: pluginInstance, field: 'documentationUrl')}"/>
                </plugin:input>

                <plugin:input
                        name="Download URL"
                        description="Where someone would click to get your plugin">
                    <input type="text" id="downloadUrl" name="downloadUrl" value="${fieldValue(bean: pluginInstance, field: 'downloadUrl')}"/>
                </plugin:input>

                <plugin:input
                        name="Officila"
                        description="Check this if you work for SpringSource">
                    <g:checkBox name="official" value="${pluginInstance?.official}"></g:checkBox>
                </plugin:input>

                </tbody>
            </table>
        </div>
        <g:submitButton name="save" value="Save"/>
    </g:form>
</div>
</body>
</html>
