<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <g:javascript src="common/application.js"/>
    <g:javascript src="common/tracking.js"/>
    
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
	
	<title>Grails - The search is over.</title>

	<g:layoutHead />

</head>
<body>
    
<div align="center">
    <div id="springSourceBar">
		<div id="springSourceLogo">
    		<a href="http://www.springsource.com/"><img src="${resource(dir:'images/new', file:'springsource-logo.jpg')}" border="0"></a>			
		</div>
    </div>

    <div class="mainMenuBarWrapper">
        <ul id="mainMenuBar">
            <li><a href="http://www.springsource.com/products">Products</a></li>
            <li><a href="http://www.springsource.com/services">Support and Services</a></li>
            <li><a href="http://www.springsource.com/training">Training</a></li>
            <li><a href="http://www.springsource.com/customer/casestudies">Case Studies</a></li>
            <li><g:link controller="content" id="Download">Downloads</g:link></li>
            <li><g:link controller="content" id="Documentation">Documentation</g:link></li>
        </ul><!-- mainMenuBar -->
    </div><!-- mainMenuBarWrapper -->
</div><!-- center -->

<div id="contentWrapper">	
    <div id="contentCenter" align="center">
        <g:layoutBody/>
    </div><!-- contentCenter -->
</div><!-- contentWrapper -->
<g:if test="${latestComments}">
	<div id="latestComments">
	    <h2><img src="${resource(dir:'images/new/plugins/icons', file:'comments.png')}" border="0" /> Most Recent Comments</h2>
	    <div id='commentList'>
	        <g:each var='comment' status='i' in="${latestComments}">
	            <g:set var='oddEven' value="${(i%2==0) ? 'even' : 'odd'}"/>
	            <div class='comment ${oddEven}'>
	                <g:render template="comment" var="comment" bean="${comment}"/>
	            </div>
	        </g:each>
	    </div>
	</div>
</g:if>

<div id="footer">
    <div align="center">
        <div class="innerFooter">
            <a href="http://twitter.com/grailsframework"><div class="twitter"></div></a>
            <a href="http://www.springsource.com"><div class="springSource"></div></a>
            <p>&copy; Copyright 2009 SpringSource.<br/>All Rights Reserved.</p>
        </div><!-- innerFooter -->
    </div><!-- center -->
</div><!-- footer -->

</body>
</html>
