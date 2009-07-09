<g:applyLayout name="pluginDetails">
<div id="contentPane">

    <h1>Edit Plugin</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${plugin}">
        <div class="errors">
            <g:renderErrors bean="${plugin}" as="list"/>
        </div>
    </g:hasErrors>

    <g:form action='editPlugin' id="${plugin?.id}">
        <input type="hidden" name="id" value="${plugin?.id}"/>
        <input type="hidden" name="version" value="${plugin?.version}"/>
        <div class="dialog">
            <table>
                <tbody>

                <plugin:input
                        name="Name"
                        description="This is what you would type in the command line after 'grails install-plugin'.">
                    <input type="text" id="name" name="name" value="${fieldValue(bean: plugin, field: 'name')}"/>
                </plugin:input>


                <plugin:input
                        name="Title"
                        description="The 'Human-Readable' title of your plugin">
                    <input type="text" id="title" name="title" value="${fieldValue(bean: plugin, field: 'title')}"/>
                </plugin:input>

                <plugin:input
                        name="Author"
                        description="Plugin author's name(s)">
                    <input type="text" id="author" name="author" value="${fieldValue(bean: plugin, field: 'author')}"/>
                </plugin:input>
                
                <plugin:input
                        name="Author Email"
                        description="Plugin author email addresses">
                    <input type="text" id="authorEmail" name="authorEmail" value="${fieldValue(bean: plugin, field: 'authorEmail')}"/>
                </plugin:input>

                <plugin:input
                        name="Grails Version"
                        description="The Grails version this plugin was developed under.">
                    <input type="text" id="grailsVersion" name="grailsVersion" value="${fieldValue(bean: plugin, field: 'grailsVersion')}"/>
                </plugin:input>

                <plugin:input
                        name="Current Release"
                        description="Current plugin release">
                    <input type="text" id="currentRelease" name="currentRelease" value="${fieldValue(bean: plugin, field: 'currentRelease')}"/>
                </plugin:input>

                <plugin:input
                        name="Documentation URL"
                        description="Where on the web is are your docs?  If this is it, then leave blank.">
                    <input type="text" id="documentationUrl" name="documentationUrl" value="${fieldValue(bean: plugin, field: 'documentationUrl')}"/>
                </plugin:input>

                <plugin:input
                        name="Download URL"
                        description="Where someone would click to get your plugin">
                    <input type="text" id="downloadUrl" name="downloadUrl" value="${fieldValue(bean: plugin, field: 'downloadUrl')}"/>
                </plugin:input>

                </tbody>
            </table>
        </div>
        <g:submitButton name="save" value="Save"/>
        %{--<div class="buttons">--}%
            %{--<span class="button"><g:actionSubmit class="save" value="Update"/></span>--}%
            %{--<span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete"/></span>--}%
        %{--</div>--}%
    </g:form>

</div>


</g:applyLayout>
