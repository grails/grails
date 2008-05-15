<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>${content?.title}</title>
    <meta content="newsLayout" name="layout" />
    <style type="text/css">

        #content-container {
            height:750px;
        }
    </style>
</head>
<body>
    <div id="contentPane">
        <h1>News: ${content?.title}</h1>
        <div style="width:450px;">
            <wiki:text key="${content?.title}">
                ${content?.body}
            </wiki:text>
        </div>
        <div class="endsection" style="margin-top:10px;">
                    <b>Posted at ${content.dateCreated}</b> by

                    <a href="#">${content.author?.login}</a>                
        </div>
    </div>
</body>
</html>