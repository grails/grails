<%@ page import="org.grails.wiki.WikiPage" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Create Page</title>
    <meta content="subpage" name="layout" />
    <g:javascript library="scriptaculous" />
</head>
<body>
    <div id="editPane">
        <h1>Create Page: ${pageName}</h1>
        <g:hasErrors bean="${wikiPage}">
            <div id="errors" class="errors">
                <g:renderErrors bean="${wikiPage}"></g:renderErrors>
            </div>
        </g:hasErrors>

        <g:form name="createWikiPage" url="[controller:'content', action:'saveWikiPage', id:pageName]">
            <g:render template="wikiFields" model="[wikiPage:new WikiPage(title:pageName)]" />
            <g:submitButton name="save" value="Save" />
        </g:form>
    </div>
    <script type="text/javascript">
        if($("errors")!=null) {
            Effect.Fade("errors", {delay:3})
        }
    </script>
</body>
</html>