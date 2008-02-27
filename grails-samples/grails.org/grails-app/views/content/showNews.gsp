<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>${content?.title}</title>
    <meta content="subpage" name="layout" />
</head>
<body>
    <div id="contentPane">
        <h1>News: ${content?.title}</h1>
        <wiki:text key="${content?.title}">
                ${content?.body}
        </wiki:text>
    </div>
</body>
</html>