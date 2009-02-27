<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'ratings.css')}" />
    <title>Grails Plugins</title>
    <meta content="subpage" name="layout"/>
</head>
<body>
<div id="contentPane">
    <div id="infoLinks" style="margin-left:520px;">
        <g:link controller="plugin" action="createPlugin">Create Plugin Page</g:link>
    </div>

    <g:render template='popularTags' var='tags' bean="${popularTags}"/>
    <g:render template='popularPlugins' var='plugins' bean="${popularPlugins}"/>
    <g:render template='newest' var='plugins' bean="${newestPlugins}"/>
    <g:render template='recentlyUpdated' var='plugins' bean="${recentlyUpdatedPlugins}"/>

</div>
</body>
</html>
