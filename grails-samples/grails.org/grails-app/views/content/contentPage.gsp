<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <gui:resources javascript='yahoo-dom-event,animation'/>
    <title>${content?.title}</title>
    <meta content="subpage" name="layout" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'content.css')}" />
    <gui:resources components="['tabView','dialog','autoComplete','paginator']" javascript='animation'/>
    <g:javascript library="diff_match_patch" />
    <g:javascript library="scriptaculous" />
	<yui:javascript dir="paginator" file="paginator-debug.js" />
    <yui:javascript dir='yahoo-dom-event' file='yahoo-dom-event.js'/>
    <g:render template="wikiJavaScript"/>
	<style type="text/css" media="screen">
		body {
		    font-family: Lucida Grande, Lucida, sans-serif;
		    font-size: 12pt;
		}
		
	</style>

</head>
<body>
    <div id="contentPane">
        <g:render template="viewActions" model="[content:content]" />
        <div id="editPane" style="margin-top:10px;">
            <wiki:text key="${content?.title}">
                ${content?.body}
            </wiki:text>
        </div>
    </div>

    <g:render template="previewPane"/>


</body>
</html>