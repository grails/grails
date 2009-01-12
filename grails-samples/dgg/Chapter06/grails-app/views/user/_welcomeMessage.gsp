<div style="margin-top:20px">
	<div style="float:right;">
		<g:link controller="user" action="profile" id="${session.user.id}">Profile</g:link> | <g:link controller="user" action="logout">Logout</g:link><br>						
	</div>

	Welcome back <span id="userFirstName">${session?.user?.firstName}!</span><br><br>

	You have purchased (${session.user.purchasedSongs?.size() ?: 0}) songs.<br>

</div>
