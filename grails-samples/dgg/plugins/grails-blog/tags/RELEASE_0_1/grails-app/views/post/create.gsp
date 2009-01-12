<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="${params.layout ?: 'main'}" />
        <title>Create Post</title>         
    </head>
    <body>
        <div class="body">
            <h1>Create Post</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${post}">
            <div class="errors">
                <g:renderErrors bean="${post}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
				   <div id="titleField">
                          <label for="title">Title:</label>
                          <g:textField name="title" value="${fieldValue(bean:post,field:'title')}"/>					   	
				   </div>
					<div id="bodyField">
						<fck:editor name="body" width="500" height="300" toolbar="Basic">${fieldValue(bean:post,field:'title')}</fck:editor>							
					</div>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Post" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
