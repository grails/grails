<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <feed:meta kind="atom" version="1.0" controller="plugin" action="feed"/>
    <g:javascript library="diff_match_patch"/>
    <g:javascript library="scriptaculous"/>
    <rateable:resources />
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'content.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'plugins.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'ratings.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'pluginHome.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'tagCloud.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'comments.css')}"/>
    <title>Grails Plugins</title>
    <meta content="plugin" name="layout"/>
    <g:render template="../content/wikiJavaScript"/>
</head>
<body>

<div id="pluginMenu">
    <h1>Plugins</h1>
    <div class="description">
        <p/>Welcome to the Grails plugin portal. The place where you can find information about the latest plugins available for the Grails framework.
    </div>
    <ul id="pluginSorters">
        <li class="all">All</li>
        <li class="featured">Featured</li>
        <li class="popular">Most Popular</li>
        <li class="recentlyUpdated">Recently Updated</li>
        <li class="newest">Newest</li>
    </ul>
    <div class="createPlugin">
        <h3>Want to create a plugin?</h3>
        <p/>For a more detailed overview of creating Grails plugins (it's easy!), checkout the user guide section on Plugins.
        <p/>If you are interested in distributing a plugin in the Grails central repository take a look at this page.
    </div>
    <div class="links">
        <h3>Links heading</h3>
        <ul>
            <li><a href="#">Link one</a></li>
            <li><a href="#">Link 2</a></li>
            <li><a href="#">Link tres</a></li>
            <li><a href="#">Long link name that runs down into multiple lines</a></li>
            <li><a href="http://dangertree.net">Dangertree!</a></li>
        </ul>
    </div>
</div>

<div id="searchBar">
    <g:render template="searchBar"/>
</div>

<div id="featuredPlugins">
    <g:each var="plugin" in="${featuredPlugins}">
        <div class="featuredPlugin">
            <h4>${plugin.title}</h4>
            <g:if test="${plugin.official}">
                <div class="supported">supported by SpringSource</div>
            </g:if>
            <div class="ratings">
                <rateable:ratings bean="${plugin}" active='false'/>
            </div>
            <div class="screenshot">
                screenshot would go here if there is one
            </div>
            <div class="details">
                <dl>
                    <dt>Tags:</dt><dd>${plugin.tags.join(', ')}</dd>
                    <dt>Grails Version:</dt><dd>${plugin.grailsVersion}</dd>
                    <dt>Current Release:</dt><dd>${plugin.currentRelease}</dd>
                </dl>
                <a href="${plugin.fisheye}"><div class="fisheye">Fisheye</div></a>
                <a href="${plugin.documentationUrl}"><div class="docs">Docs</div></a>
            </div>
        </div>
    </g:each>
</div>

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

%{--
<div id="contentPane">

    <ul id="infoLinks">
        <li class='list'>
            <g:link controller="plugin" action="list">All Plugins</g:link>
        </li>
        <li class="create">
            <g:link controller="plugin" action="createPlugin">Create Plugin Page</g:link>
        </li>
        <li class="feed">
            <g:link controller="plugin" action="latest" params="[format:'rss']">Feed</g:link>
        </li>
    </ul>

    <h1>Grails Plugins</h1>

    <g:render template="searchBar"/>

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

    <tags:cloud
            type='plugin'
            minSize='8'
            maxSize='44'
            class='pluginTagCloud'
            linkClass='tagLink'
            controller='plugin'
            action='showTag'/>

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
--}%

</body>
</html>
