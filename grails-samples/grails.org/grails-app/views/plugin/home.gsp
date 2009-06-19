<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <feed:meta kind="atom" version="1.0" controller="plugin" action="feed"/>
    <g:javascript library="diff_match_patch"/>
    <g:javascript library="scriptaculous"/>
    <rateable:resources />
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'ratings.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css/new', file: 'comments.css')}"/>
    <title>Grails Plugins</title>
    <meta content="plugin" name="layout"/>
    <g:render template="../content/wikiJavaScript"/>
</head>
<body>

<div id="contentArea">
    <div id="pluginMenu">
        <h1>Plugins</h1>
        <div class="box">
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
                <div class="detail">
                    If you are interested in creating and distributing a plugin in the Grails central repository, take a look at this <a href="#">user guide section</a>.
                </div>
            </div>
            <div class="bottom"></div>
        </div><!-- box -->
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

    <div id="content">
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
    </div>
</div><!-- contentArea -->

<div id="btmSectionGraphicsWrapper">
    <div id="mountainLeft"></div>
    <div id="knight"></div>
    <div id="mountainRight"></div>
    <div id="castle"></div>
</div><!-- btmSectionGraphicsWrapper-->
<div id="btmSectionBackgroundStretch">
    <div id="latestComments">
        <h2>Most Recent Comments</h2>
        <ul id='commentList'>
            <g:each var='comment' status='i' in="${latestComments}">
                <g:set var='oddEven' value="${(i%2==0) ? 'even' : 'odd'}"/>
                <li class='comment ${oddEven}'>
                    <g:render template="shortComment" var="comment" bean="${comment}"/>
                </li>
            </g:each>
        </ul>
    </div>
</div><!-- btmSectionBackgroundStretch -->



</body>
</html>
