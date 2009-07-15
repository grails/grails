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
            color:#48802C;
        }



    </style>
</head>
<body>
    <div id="contentPane">
		<g:render template="download" model="[downloadObj:stableDownload, docDownload:docDownload, title:'Current Stable Release']"></g:render>

		<g:if test="${betaDownload}">
			<g:render template="download" model="[downloadObj:betaDownload, docDownload:betaDoc, title:'Current Development Release']"></g:render>
		</g:if>
 



        <p>Past releases can be found at the <g:link controller="download" action="archive" id="Grails">Archive</g:link>.</p>
        <br />

        <h3>Grails Development Builds</h3>
        <p>

            You can obtain a development build of Grails from our Hudson continuous integration server at <a href="http://hudson.grails.org" class="pageLink">http://hudson.grails.org</a>
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