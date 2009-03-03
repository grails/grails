<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Grails Plugin Tag Cloud</title>
    <meta content="subpage" name="layout"/>
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'plugins.css')}" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'tagCloud.css')}" />
</head>
<body>
<div id="contentPane">

    <ul id="infoLinks">
        <li class="home">
            <g:link controller="plugin" action="index">Plugins Home</g:link><br/>
        </li>
        <li class='list'>
            <g:link controller="plugin" action="list">All Plugins</g:link>
        </li>
    </ul>

    <richui:tagCloud
        values="${tagCounts}"
        minSize='8'
        maxSize='48'
        class='pluginTagCloud'
        linkClass='tagLink'
        controller='tag'
        action='show'
    />

</div>
</body>
</html>
