<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="${params.layout ?: 'main'}" />
        <title>Post List</title>
    </head>
    <body>
	
        <div class="nav">
           	<span class="menuButton"><g:link class="create" action="create">New Post</g:link></span>	
        </div>
        <div class="blog">
            <h1>${grailsApplication.config.blog.title ?: 'No Title'}</h1>
			
			<g:render plugin="blog" template="post" var="post" collection="${postList?.reverse()}"></g:render>
        </div>
    </body>
</html>
