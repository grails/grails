<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'plugins.css')}" />    
    <title>${plugin.title} Plugin</title>
    <meta content="subpage" name="layout"/>
</head>
<body>
<div id="contentPane">
    <div id="infoLinks" style="margin-left:520px;">
        <g:link controller="plugin" action="list">All Plugins</g:link><br/>
        <g:link controller="plugin" action="editPlugin" params="${[name:plugin.name]}"><img src="${createLinkTo(dir: 'images/', 'icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0"/>Edit Plugin</g:link>
    </div>

    <h1>${plugin?.title}</h1>
    <div class="plugin">

        <h2>Description</h2>
        <wiki:text>
            ${plugin?.description}
        </wiki:text>
        <h2>FAQ</h2>
        <wiki:text>
            ${plugin?.faq}
        </wiki:text>
        <h2>Screenshots</h2>
        <wiki:text>
            ${plugin?.screenshots}
        </wiki:text>
        <h2>Installation</h2>
        <wiki:text>
            ${plugin?.installation}
        </wiki:text>

        <g:each var="comment" in="${plugin?.comments}">
            <div class="comment">
                <wiki:text>
                    ${comment?.body}
                </wiki:text>
                <div class="author">${comment?.user.login}</div>
            </div>
        </g:each>
    </div>
</div>
</body>
</html>