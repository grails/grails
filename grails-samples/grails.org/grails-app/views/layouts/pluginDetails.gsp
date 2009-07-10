<%@ page import="org.grails.plugin.Plugin" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <rateable:resources />
    <gui:resources components="['tabView','dialog','autoComplete','paginator']" javascript='animation'/>
    <g:javascript library="diff_match_patch" />
    <g:javascript library="scriptaculous" />
	<yui:javascript dir="paginator" file="paginator-debug.js" />
	
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'tabview.css')}" />

    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'content.css')}" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'plugins.css')}" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css/new',file:'comments.css')}" />

    <title>Plugin: ${plugin.title}</title>
    <meta content="pluginInfoLayout" name="layout"/>
    <g:render template="../content/wikiJavaScript"/>    

	<style type="text/css" media="screen">
		.yui-navset-bottom .yui-nav li a em {
			display:inline;
		}
		.yui-navset .yui-nav li a em, .yui-navset-top .yui-nav li a em, .yui-navset-bottom .yui-nav li a em {
			display:inline;
		}		
		
	</style>
</head>
<body>

<div id="contentPane">

	<div id="pluginBigBox">
		<div id="pluginBgTop"></div>
		<div id="pluginBox">
			<div id="pluginDetailWrapper">

			
				<g:layoutBody />
				<div class="pluginBoxBottom">

				</div>
				<div id="btmSectionGraphicsWrapper">
				    <%
				        def commentHeader = "No Comments Yet"
				        if (plugin.comments.size == 1) commentHeader = "1 Comment"
				        if (plugin.comments.size > 1) commentHeader = "${plugin.comments.size()} Comments"
				    %>
					
				    <div id="mountainLeft"></div>
				    <div id="knight"></div>
				    <div id="mountainRight"></div>
				    <div id="castle"></div>
				    <h2 class="commentsHeader"><a class='anchor' name='comments'><img src="${resource(dir:'images/new/plugins/icons', file:'comments.png')}" border="0" /> ${commentHeader}</a></h2>
				
				</div><!-- btmSectionGraphicsWrapper-->				

        		<g:render template="../comments/comments" model="${[commentType:'plugin', parentId:plugin.id, comments:plugin.comments]}"/>						

				
				<div id="footer">
				    <div align="center">
				        <div class="innerFooter">
				            <a href="http://twitter.com/grailsframework"><div class="twitter"></div></a>
				            <a href="http://www.springsource.com"><div class="springSource"></div></a>
				            <p>&copy; Copyright 2009 SpringSource.<br/>All Rights Reserved.</p>
				        </div><!-- innerFooter -->
				    </div><!-- center -->
				</div><!-- footer -->
		        <g:render template="../content/previewPane"/>


				
		    </div>	

		</div>		
	</div>
</div>
</body>
</html>