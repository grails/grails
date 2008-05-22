<%@ page import="org.grails.wiki.WikiPage" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Edit News</title>
    <meta content="subpage" name="layout" />
    <g:javascript library="scriptaculous" />
</head>
<body>
    <div id="editPane">
        <h1>Title: ${content.title}</h1>
        <g:form action="editNews" id="${content?.id}">
            <g:textArea id="body" name="body" rows="20" cols="75" value="${content?.body}" /> <br />
            <g:submitButton name="save" value="Save" />
        </g:form>
    </div>
</body>
</html>