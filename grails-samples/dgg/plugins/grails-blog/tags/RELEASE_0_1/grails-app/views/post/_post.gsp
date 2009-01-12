<div id="post${post.id}" class="blogPost">
	<h2>${post.title}</h2>
	<div class="body">
		${post.body}
	</div>
	
	<div class="desc">
		Posted on <g:formatDate date="${post.dateCreated}" format="dd MMMMMM yy"></g:formatDate>
	</div>
	
</div>