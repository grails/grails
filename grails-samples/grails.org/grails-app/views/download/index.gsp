<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Download Grails</title>
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
        <h1>Download Grails</h1>
        <h3>Current Stable Release: ${download?.softwareVersion}</h3>
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

            <g:set var="docFile" value="${docDownload?.files?.iterator()?.next()}"></g:set>
            <g:if test="${docFile}">
               <g:form controller="download" action="downloadFile">
                    <tr>
                    <td><strong>Documentation</strong></td>
                    <td>
                        <g:select optionKey="id" optionValue="name" name="mirror" from="${docFile.mirrors}" />

                    </td>
                    <td width="70" class="downloadCell"> <g:submitButton name="Download" value="Download" /></td>
                </tr>
                </g:form>
            </g:if>
        </table>

        

        <p>Got a mirror? <a href="http://www.g2one.com/company.html#contactus">Contact G2One Inc.</a> to get it posted. </p>

        <p>Past releases can be found at the <g:link controller="download" action="archive" id="Grails">Archive</g:link>.</p>
        <br />

        <h3>Grails Development Builds</h3>
        <p>

            You can obtain a development build of Grails from our Bamboo continuous integration server at <a href="http://bamboo.ci.codehaus.org/browse/GRAILS" class="pageLink">http://bamboo.ci.codehaus.org/browse/GRAILS</a>
        </p>
        <p class="paragraph">Follow these steps:
        </p>

        <ul class="star">
            <li>Click on the latest build link relevant to your JDK</li>
            <li>Click on the Completed Builds tab</li>
            <li>Click on the latest successful build link (green, not red)</li>
            <li>Click on the Artifacts tab</li>
            <li>Click on the Distributions link</li>
            <li>Download a relevant snapshot form the list provided</li>
        </ul>

        <br />

        <h3>Grails Plugin Downloads</h3>

        <p>Grails has a number of plugins available for it that extend its capability. Check out the <g:link controller="content" id="Plugins">Plugins page</g:link> for more info on available plugins and how they can be installed.</p>
    </div>
</body>
</html>