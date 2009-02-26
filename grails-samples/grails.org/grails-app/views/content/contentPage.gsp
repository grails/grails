<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <gui:resources javascript='yahoo-dom-event,animation'/>
    <title>${content?.title}</title>
    <meta content="subpage" name="layout" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'content.css')}" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'comments.css')}" />
    <g:javascript library="scriptaculous" />
    <g:javascript library="diff_match_patch" />
    <yui:javascript dir='yahoo-dom-event' file='yahoo-dom-event.js'/>
    <g:render template="wikiJavaScript"/>

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

    <g:render template="../comment/comments" model="${[commentType:'content', parentId:content.id, comments:comments, locked:content.locked]}"/>

</body>
</html>