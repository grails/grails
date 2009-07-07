<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <feed:meta kind="atom" version="1.0" controller="plugin" action="feed"/>
    <g:javascript library="diff_match_patch"/>
    <g:javascript library="scriptaculous"/>
    <rateable:resources />
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'ratings.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css/new', file: 'comments.css')}"/>
    <title>Grails Plugins</title>
    <meta content="pluginLayout" name="layout"/>
    <g:render template="../content/wikiJavaScript"/>
</head>
<body>

<g:applyLayout name="pluginNav">
	<div id="currentPlugins">
	    <g:each var="plugin" in="${currentPlugins}">
			<tmpl:pluginPreview plugin="${plugin}" />
	    </g:each>
	</div>
	<div id="paginationPlugins">
		<g:paginate total="${totalPlugins}" params="[category:category]" next="&gt;" prev="&lt;"></g:paginate>
	</div>
</g:applyLayout>



</body>
</html>
