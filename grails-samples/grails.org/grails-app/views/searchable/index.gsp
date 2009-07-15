<%@ page import="org.grails.plugin.Plugin" %>
<%@ page import="org.springframework.util.ClassUtils" %>
<%@ page import="org.codehaus.groovy.grails.plugins.searchable.SearchableUtils" %>
<%@ page import="org.codehaus.groovy.grails.plugins.searchable.lucene.LuceneUtils" %>
<html>
<head>
    <meta content="subpage" name="layout"/>
    <title>Search Results</title>
    <script type="text/javascript">
        var focusQueryInput = function() {
            document.getElementById("q").focus();
        }
    </script>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'search.css')}"/>
</head>
<body onload="focusQueryInput();">
<div id="header">
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
                <g:else>
                    &nbsp;
                </g:else>
            </span>
        </div>

        <g:if test="${parseException}">
            <p>Your query - <strong>${query}</strong> - is not valid.</p>
        </g:if>
        <g:elseif test="${haveQuery && !haveResults}">
            <p>Nothing matched your query - <strong>${query}</strong></p>
        </g:elseif>
        <g:elseif test="${haveResults}">
            <div id="results" class="results">
                <g:each var="result" in="${searchResult.results}" status="index">
                    <div class="result">

                        <g:set var="className" value="${result.title}"/>

                        <div class="name">

                            %{--
                                The result may be either a WikiPage or Plugin object.  If it is a Plugin, we'll want to
                                link it properly to the Plugin domain.  Otherwise it gets treated like a normal WikiPage
                            --}%
                            <g:if test="${result instanceof Plugin}">
                                <g:link controller="plugin" action="show" params="${[name:result.name]}">${className}</g:link>
                            </g:if>
                            <g:else>
                                <g:link controller="content" id="${result.title}">${className}</g:link>
                            </g:else>
                        </div>

                        <g:set var="desc"><g:if test="${result.body?.size() > 220}"><wiki:text>${result.body[0..220]}</wiki:text>...</g:if>
                            <g:else><wiki:text id="${result.title}">${result.body ?: ''}</wiki:text></g:else></g:set>
                        <div class="desc">${desc}</div>

                    </div>
                </g:each>
            </div>

            <div>
                <div class="paging">
                    <g:if test="${haveResults}">
                        Page:
                        <g:set var="totalPages" value="${Math.ceil(searchResult.total / searchResult.max)}"/>
                        <g:if test="${totalPages == 1}"><span class="currentStep">1</span></g:if>
                        <g:else><g:paginate controller="searchable" action="index" params="[q: params.q]" total="${searchResult.total}" prev="&lt; previous" next="next &gt;"/></g:else>
                    </g:if>
                </div>
            </div>
        </g:elseif>
    </div>
</div>
</body>
</html>