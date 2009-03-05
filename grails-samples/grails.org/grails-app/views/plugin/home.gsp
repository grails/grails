<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <feed:meta kind="atom" version="1.0" controller="plugin" action="feed"/>
    <g:javascript library="diff_match_patch"/>
    <g:javascript library="scriptaculous"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'content.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'plugins.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'ratings.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'pluginHome.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'tagCloud.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'comments.css')}"/>
    <title>Grails Plugins</title>
    <meta content="subpage" name="layout"/>
    <g:render template="../content/wikiJavaScript"/>
</head>
<body>
<div id="contentPane">

    <ul id="infoLinks">
        <li class='list'>
            <g:link controller="plugin" action="list">All Plugins</g:link>
        </li>
        <li class="create">
            <g:link controller="plugin" action="createPlugin">Create Plugin Page</g:link>
        </li>
        <li class="feed">
            <g:link controller="plugin" action="feed" params="[format:'rss']">Feed</g:link>
        </li>
    </ul>

    <h1>Grails Plugins</h1>

    <g:render template="searchBar"/>

    <richui:tagCloud
            values="${tagCounts}"
            minSize='8'
            maxSize='48'
            class='pluginTagCloud'
            linkClass='tagLink'
            controller='tag'
            action='show'/>

    <table id="statsTable">
        <tbody>
        <tr>
            <td class='section'>
                <g:render template='popularPlugins' var='plugins' bean="${popularPlugins}"/>
            </td>
            <td class='section'>
                <g:render template='newest' var='plugins' bean="${newestPlugins}"/>
            </td>
            <td class='section'>
                <g:render template='recentlyUpdated' var='plugins' bean="${recentlyUpdatedPlugins}"/>
            </td>
        </tr>
        </tbody>
    </table>

    <div id="pluginHomeWiki">
        <g:render template="../content/viewActions" model="${[content: homeWiki, update: 'pluginHomeWiki']}"/>
        <div class='wikiPage'><wiki:text>${homeWiki}</wiki:text></div>
    </div>
    <g:render template="../content/previewPane"/>

    <div id="latestComments">
        <h2>Latest Comments</h2>
        <ul id='commentList'>
            <g:each var='comment' status='i' in="${latestComments}">
                <g:set var='oddEven' value="${(i%2==0) ? 'even' : 'odd'}"/>
                <li class='comment ${oddEven}'>
                    <g:render template="shortComment" var="comment" bean="${comment}"/>
                </li>
            </g:each>
        </ul>
    </div>

</div>
</body>
</html>
