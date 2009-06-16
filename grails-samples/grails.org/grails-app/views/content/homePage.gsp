<%@ page import="org.grails.news.NewsItem; org.grails.auth.Role" %><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
    <meta content="homepage" name="layout" />
</head
<body>

%{--
	  <div id="feedLink" >
	       <g:link controller="blog" action="feed" params="[format:'rss']"><img border="0" src="${createLinkTo(dir:'images', file:'feed.gif')}" alt="RSS Feed"  /></g:link>
	  </div>
	 <div class="newsItems">
        <div style="float:right; margin-right:10px">
            <g:link controller="blog" action="createEntry">Click here</g:link> to add news
        </div>
		
	 	<blog:renderEntries number="3" />		
	 </div>
	 <div id="trainingEvents">
	 	<wiki:text page="Training Events" />
	 </div>
--}%

</body>
</html>