<%@ page import="org.grails.plugin.Plugin" %>
<%@ page import="org.springframework.util.ClassUtils" %>
<%@ page import="org.codehaus.groovy.grails.plugins.searchable.SearchableUtils" %>
<%@ page import="org.codehaus.groovy.grails.plugins.searchable.lucene.LuceneUtils" %>
<html>
<head>
    <meta content="pluginLayout" name="layout"/>
    <title>Plugin Search Results</title>
    <script type="text/javascript">
        var focusQueryInput = function() {
            document.getElementById("q").focus();
        }
    </script>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'content.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'plugins.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'ratings.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'search.css')}"/>
</head>
<body onload="focusQueryInput();">
<g:applyLayout name="pluginNav">
<div id="main">
    <g:set var="haveQuery" value="${params.q?.trim()}"/>
    <g:set var="haveResults" value="${searchResult?.results}"/>
    <g:set var="query" value="${params.q?.encodeAsHTML()}"/>
    <div class="title">
        <span>
            <g:if test="${haveQuery && haveResults}">
                Showing <strong>${searchResult.offset + 1}</strong> - <strong>${searchResult.results.size() + searchResult.offset}</strong> of <strong>${searchResult.total}</strong>
                results for <strong>${query}</strong>
            </g:if>
		    <g:if test="${parseException}">
		        <p>Your query - <strong>${query}</strong> - is not valid.</p>
		    </g:if>
		    <g:elseif test="${haveQuery && !haveResults}">
		        <p>Nothing matched your query - <strong>${query}</strong></p>
		    </g:elseif>
        </span>
    </div>


    <g:if test="${haveResults}">
        <div id="results" class="results">
            <g:each var="result" in="${searchResult.results}" status="index">
				<tmpl:pluginPreview plugin="${result}" />
            </g:each>
        </div>

        <div>
            <div class="paging">
                <g:if test="${haveResults}">
                    Page:
                    <g:set var="totalPages" value="${Math.ceil(searchResult.total / searchResult.max)}"/>
                    <g:if test="${totalPages == 1}"><span class="currentStep">1</span></g:if>
                    <g:else><g:paginate controller="plugin" action="search" params="[q: params.q]" total="${searchResult.total}" prev="&lt; previous" next="next &gt;"/></g:else>
                </g:if>
            </div>
        </div>
    </g:if>
</div>
</g:applyLayout>
</body>
</html>