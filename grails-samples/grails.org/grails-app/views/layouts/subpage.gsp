<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<meta name="robots" content="NOODP">	
	<meta name="Description" content="Grails is a high-productivity web framework based on the Groovy language that embraces the coding by convention paradigm, but is designed specifically for the Java platform.">	
	
	<title>Grails - <g:layoutTitle default="The search is over." /></title>

	<link rel="stylesheet" href="${createLinkTo(dir:'css', file:'master.css')}" type="text/css" media="screen" title="Master screen stylesheet" charset="utf-8" />
	<style type="text/css">@import url("${createLinkTo(dir:'css', file:'subpage.css')}");</style>
	<script type="text/javascript">
	var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
	document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
	</script>
	<script type="text/javascript">
	try {
	var pageTracker = _gat._getTracker("UA-2728886-12");
	pageTracker._trackPageview();
	} catch(err) {}</script>	

	<!--[if IE]>
		<link rel="stylesheet" href="${createLinkTo(dir:'css/ie', file:'master.css')}" type="text/css" media="screen" title="Primary screen stylesheet" charset="utf-8" />
	<![endif]-->
    <g:javascript library="prototype" />

    <g:layoutHead />

</head>
<body class="subpage">

	<div id="container">
        <jsec:isLoggedIn>
            <div id="statusbox">Welcome <strong><jsec:principal /></strong> | <g:link controller="user" action="profile">Profile</g:link> | <g:link controller="user" action="logout">Logout</g:link></div>    
        </jsec:isLoggedIn>
		<div id="floatBox">
            <g:render template="/content/nav" />
		</div><!-- / floatBox -->

        <g:render template="/content/ads" />

		<!-- logo -->
		<g:link controller="content" id="Home"><img src="${createLinkTo(dir:'images',file:'grails-logo-sm.png')}" width="196" height="53" alt="Smaller Grails Logo" class="logo" border="0" /></g:link>
		<!-- / logo -->
        <g:set var="title" value="${layoutTitle()}" />
        <g:if test="${title?.size() < 20}">
            <h1>${title}</h1>
        </g:if>

		<div id="breadcrumb">
		<ul>
			<li>> <a href=""><g:layoutTitle /></a>
			</li>
		</ul>
		</div><!-- / breadcrumb -->

		<div id="content">
			<div id="content-container">
                 <g:layoutBody />
		    </div>
		</div><!-- / content -->

		<div class="push"></div>

	</div><!-- /container -->

    <g:render template="/content/footer" />

</body>
</html>