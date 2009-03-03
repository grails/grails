<%@ page import="org.grails.plugin.Plugin" %>
<%@ page import="org.springframework.util.ClassUtils" %>
<%@ page import="org.codehaus.groovy.grails.plugins.searchable.SearchableUtils" %>
<%@ page import="org.codehaus.groovy.grails.plugins.searchable.lucene.LuceneUtils" %>
<html>
<head>
    <meta content="subpage" name="layout"/>
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
<div id="contentPane">
    <ul id="infoLinks">
        <li class="home">
            <g:link controller="plugin" action="index">Plugins Home</g:link><br/>
        </li>
    </ul>
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

        <g:render template="searchBar"/>

        <g:if test="${parseException}">
            <p>Your query - <strong>${query}</strong> - is not valid.</p>
        </g:if>
        <g:elseif test="${haveQuery && !haveResults}">
            <p>Nothing matched your query - <strong>${query}</strong></p>
        </g:elseif>
        <g:elseif test="${haveResults}">
            <div id="results" class="results">
                <g:each var="result" in="${searchResult.results}" status="index">
                    <div class="plugin result">

                        <g:set var="className" value="${result.title}"/>

                        <div class="name">
                            <g:link controller="plugin" action="show" params="${[name:result.name]}">${className}</g:link>

                        </div>

                        <table class='search'>
                            <tr>
                                <th>Author(s)</th>
                                <td>${result.author}</td>
                                <th>Current Release</th>
                                <td>${result.currentRelease}</td>
                            </tr>

                            <tr>
                                <th>Rating</th>
                                <td>
                                    <g:render template="ratingDisplay" var="average" bean="${result.avgRating}"/>
                                </td>
                                <th>Tags</th>
                                <td>
                                    <span id='pluginTags'>
                                        <g:render template='tags' var='plugin' bean="${result}"/>
                                    </span>
                                </td>
                            </tr>
                        </table>

                        <g:set var="desc"><g:if test="${result.description.body?.size() > 220}"><wiki:text>${result.description.body[0..220]}</wiki:text>...</g:if>
                            <g:else><wiki:text id="${result.title}">${result.description.body ?: ''}</wiki:text></g:else></g:set>
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
                        <g:else><g:paginate controller="plugin" action="search" params="[q: params.q]" total="${searchResult.total}" prev="&lt; previous" next="next &gt;"/></g:else>
                    </g:if>
                </div>
            </div>
        </g:elseif>
    </div>
</div>
</body>
</html>