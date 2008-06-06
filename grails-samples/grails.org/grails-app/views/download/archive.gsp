<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Download Archive</title>
    <meta content="subpage" name="layout" />
    <g:javascript library="scriptaculous" />
    <style type="text/css">

        .download-table  {

            margin:10px;
            padding:5px;
            width:90%;
            margin-bottom:40px;
        }
        .download-table td  {
            border-top:0px;
            margin:10px;
            padding:5px;

        }

        .download-table th  {
            background-color: lightgray;
            border:none;
            border-top:0px;
            margin:10px;
            padding:5px;
            color:#014684;
        }



    </style>
</head>
<body>
    <div id="contentPane">
        <h1>Download Archive</h1>

        <g:each var="download" in="${downloads?}">

            <h3>Grails ${download?.softwareVersion}</h3>
            <ul>
                <li><a href="http://jira.codehaus.org/browse/GRAILS?report=com.atlassian.jira.plugin.system.project:changelog-panel">Change Log</a></li>
                <li><a href="${download?.releaseNotes}">Release Notes</a></li>
            </ul>

            <table class="download-table">
                <tr><th>Distribution</th><th>Mirror</th></tr>
                <g:each var="file" in="${download?.files}">
                    <g:form controller="download" action="downloadFile">
                        <tr>
                            <td><strong>${file.title}</strong></td>
                            <td>
                                <g:select optionKey="id" optionValue="name" name="mirror" from="${file.mirrors}" />

                            </td>
                            <td width="70" class="downloadCell"> <g:submitButton name="Download" value="Download" /></td>
                        </tr>
                    </g:form>
                </g:each>
            </table>
        </g:each>

        <p>Past releases and individual JAR downloads can also be found at the <a href="http://dist.codehaus.org/grails/">Codehaus distribution site</a>.</p>
    </div>
</body>
</html>