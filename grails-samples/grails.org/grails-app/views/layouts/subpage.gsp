<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<title>Grails - The search is over.</title>

	<link rel="stylesheet" href="${createLinkTo(dir:'css', file:'master.css')}" type="text/css" media="screen" title="Master screen stylesheet" charset="utf-8" />
	<style type="text/css">@import url("${createLinkTo(dir:'css', file:'subpage.css')}");</style>

	<!--[if IE]>
		<link rel="stylesheet" href="css/ie/master.css" type="text/css" media="screen" title="Primary screen stylesheet" charset="utf-8" />
	<![endif]-->
    <g:javascript library="prototype" />

    <g:layoutHead />

</head>
<body class="subpage">

	<div id="container">

		<div id="floatBox">
            <g:render template="/content/nav" />
		</div><!-- / floatBox -->

        <g:render template="/content/ads" />

		<!-- logo -->
		<g:link controller="content" id="Home"><img src="${createLinkTo(dir:'images',file:'grails-logo-sm.png')}" width="196" height="53" alt="Smaller Grails Logo" class="logo" border="0" /></g:link>
		<!-- / logo -->

		<h1><g:layoutTitle /></h1>

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