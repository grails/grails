<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <g:javascript src="common/application.js"/>
	<g:render template="/content/analytics" />
    
    <link rel="stylesheet" href="${createLinkTo(dir: 'css/new', file: 'master.css')}" type="text/css" />
    <link rel="stylesheet" href="${createLinkTo(dir: 'css/new', file: 'plugins.css')}" type="text/css" />
	<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
	<link rel="icon" href="/images/favicon.ico" type="image/x-icon">

	<!--[if IE]>
    <link rel="stylesheet" href="${resource(dir: 'css/new', file: 'ie.css')}"/>
	<![endif]-->

    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<meta name="robots" content="NOODP">	
	<meta name="Description" content="Grails is a high-productivity web framework based on the Groovy language that embraces the coding by convention paradigm, but is designed specifically for the Java platform.">	
	
	<title>Grails - Plugins Portal</title>

	<g:layoutHead />

</head>
<body>
    
<div align="center">
	<g:render template="/content/logos"></g:render>
    <div class="mainMenuBarWrapper">
		<g:render template="/content/mainMenuBar"></g:render>
    </div><!-- mainMenuBarWrapper -->
</div><!-- center -->

<div id="contentWrapper">	
    <div id="contentCenter" align="center">	
        <g:layoutBody/>
    </div><!-- contentCenter -->
</div><!-- contentWrapper -->

	<g:render template="/content/footer" />
</body>
</html>
