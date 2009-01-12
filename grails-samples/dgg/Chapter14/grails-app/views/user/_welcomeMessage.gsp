<div style="margin-top:20px">
	<div style="float:right;">
		<g:link controller="user" action="profile" id="${request.user.id}">Profile</g:link> | <g:link controller="user" action="logout">Logout</g:link><br>						
	</div>

	Welcome back <span id="userFirstName">${request?.user?.firstName}!</span><br><br>

	You have purchased (${com.g2one.gtunes.AlbumPayment.countByUser(request.user)}) albums.<br>

</div>
