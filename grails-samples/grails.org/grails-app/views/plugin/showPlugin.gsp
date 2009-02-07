<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>${plugin.title} Plugin</title>
    <meta content="subpage" name="layout"/>
</head>
<body>
<div id="contentPane">
    <div id="infoLinks" style="margin-left:520px;">
        <g:link controller="plugin" action="list">All Plugins</g:link><br/>
        <g:link controller="plugin" action="edit" id="${plugin?.id}"><img src="${createLinkTo(dir: 'images/', 'icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0"/>Edit Plugin</g:link>
    </div>

    <h1>Plugin: ${plugin?.title}</h1>
    <div style="width:450px;">
        <wiki:text key="${plugin?.title}">
            ${plugin?.body}
        </wiki:text>
    </div>
    <div class="endsection" style="margin-top:10px;">
        <b>Posted at ${plugin.dateCreated}</b> by

        <a href="#">${plugin.author}</a>
    </div>
</div>
</body>
</html>