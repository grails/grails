<%@ page import="org.grails.news.NewsItem; org.grails.auth.Role" %><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
    <meta content="main" name="layout" />
</head
<body>
    <g:each var="newsItem" in="${NewsItem.list(sort:'dateCreated', max:2)}">
        <div class="blogpost" style="margin-bottom: 30px">
            <div><g:link controller="news" action="showNews" id="${newsItem.id}">${newsItem.title}</g:link></div>

            <div class="pagesubheading">
                            </div>
                <div class="wiki-content">                    
                    <g:if test="${newsItem.body.size() > 150}">
                      <wiki:text>${newsItem.body[0..150]} ... [click for more|${createLink(controller:'news', action:'showNews', id:newsItem.id)}]</wiki:text> 
                    </g:if>
                    <g:else>
                        <wiki:text>${newsItem.body}</wiki:text>
                    </g:else>
                </div>
                 <div class="endsection">
                            <b>Posted at ${newsItem.dateCreated}</b> by

                            <a href="#">${newsItem.author?.login}</a>            |
                </div>
        </div>
    </g:each>

    <div>
        <g:link controller="news" action="createNews">Click here</g:link> to add news
    </div>

</body>
</html>