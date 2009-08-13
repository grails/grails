<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <g:javascript src="common/application.js"/>
    <g:javascript src="common/tracking.js"/>
    
	
    <link rel="stylesheet" href="${createLinkTo(dir: 'css/new', file: 'master.css')}" type="text/css" />
    <link rel="stylesheet" href="${createLinkTo(dir: 'css/new', file: 'pluginInfo.css')}" type="text/css" />
	<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
	<link rel="icon" href="/images/favicon.ico" type="image/x-icon">


    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<meta name="robots" content="NOODP">	
	<meta name="Description" content="Grails is a high-productivity web framework based on the Groovy language that embraces the coding by convention paradigm, but is designed specifically for the Java platform.">	
	
	<title><g:layoutTitle default="Grails - The search is over."></g:layoutTitle></title>


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

    <div id="contentCenter" >

        <g:layoutBody/>
    </div><!-- contentCenter -->
</div><!-- contentWrapper -->


</body>
</html>
