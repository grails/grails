<%@ page import="org.grails.plugin.Plugin" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <rateable:resources />
    <gui:resources components="['tabView','dialog','autoComplete']" javascript='animation'/>
    <g:javascript library="diff_match_patch" />
    <g:javascript library="scriptaculous" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'content.css')}" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'plugins.css')}" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'comments.css')}" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'tabview.css')}" />
    <title>Plugin: ${plugin.title}</title>
    <meta content="subpage" name="layout"/>
    <g:render template="../content/wikiJavaScript"/>    
</head>
<body>
<div id="contentPane">
    <ul id="infoLinks">
        <li class="home">
            <g:link controller="plugin" action="index">Plugins Home</g:link><br/>
        </li>
        <li class="edit">
            <g:link controller="plugin" action="editPlugin" id="${plugin.id}">Edit Plugin</g:link>
        </li>
    </ul>

    <gui:dialog id='loginDialog' title="Login required" modal="true">
        <div id='loginFormDiv'></div>
    </gui:dialog>

    <h1>${plugin?.title}</h1>

    <div class="plugin">

        <div class="roundedcornr_box">
            <div class="roundedcornr_top"><div></div></div>
            <div class="roundedcornr_content">

                <table class='details ${plugin.official ? 'official' : ''}'>
                    <tr>
                        <th>Author(s)</th>
                        <td>${plugin.author}</td>
                        <td colspan='2'>
                            <jsec:isLoggedIn>
                                ${plugin.authorEmail}
                            </jsec:isLoggedIn>
                            <jsec:isNotLoggedIn>
                                (Log in for author email address)
                            </jsec:isNotLoggedIn>
                        </td>
                    </tr>
                    <tr>
                        <th>Current Release</th>
                        <td>${plugin.currentRelease}</td>
                        <td><a href="${plugin.documentationUrl}">Official Docs</a></td>
                        <td><a href="${plugin.fisheye}">Fisheye</a></td>
                    </tr>
                    <tr>
                        <th>Built on Grails</th>
                        <td>${plugin.grailsVersion ?: '?'}</td>
                        <td colspan='2'><a href="${plugin.downloadUrl}">Download</a></td>
                    </tr>
                    <tr>
                        <th>Rating</th>
                        <td colspan='3'>
                            <rateable:ratings bean="${plugin}"/>
                        </td>
                    </tr>
                    <tr>
                        <th>Tags</th>
                        <td colspan='3' class='tags'>
                            <span id='pluginTags'>
                                <g:render template='tags' var='plugin' bean="${plugin}"/>
                            </span>
                            <span id='addTagTrigger'><img src="${createLinkTo(dir: 'images/famfamfam', file: 'add.png')}"/></span>
                        </td>
                    </tr>
                </table>

            </div>
            <div class="roundedcornr_bottom"><div></div></div>
        </div>

        <g:if test="${plugin.official}">
            <div class="supported">Supported by <a href="http://www.springsource.com">SpringSource</a></div>
        </g:if>

        %{--
            Logged in users will be able to add tags
        --}%
        <jsec:isLoggedIn>
            <gui:dialog id='addTagDialog'
                title='Add Tags'
                form='true' controller='plugin' action='addTag' params="${[id:plugin.id]}"
                triggers="[show:[id:'addTagTrigger',on:'click']]"
                update='pluginTags'
            >
                <gui:autoComplete id='newTag'
                    controller='tag' action='autoCompleteNames'
                    resultName='tagResults'
                    labelField='name'
                    minQueryLength='1'
                    queryDelay='1'
                />
            </gui:dialog>

            <script>
                YAHOO.util.Event.onDOMReady(function() {
                    // on show, put the dialog in the right place
                    GRAILSUI.addTagDialog.subscribe('show', function() {
                        var pos = YAHOO.util.Dom.getXY('addTagTrigger');
                        this.cfg.setProperty('x',pos[0]+20);
                        this.cfg.setProperty('y',pos[1]+20);
                    });
                    // on hide, clear out the text within it
                    GRAILSUI.addTagDialog.subscribe('hide', function() {
                        document.getElementById('newTag').value = ''
                    });
                });
            </script>
        </jsec:isLoggedIn>
        %{-- Unauthenticated users get defered to the login screen --}%
        <jsec:isNotLoggedIn>
            <script>
                YAHOO.util.Event.onDOMReady(function() {
                    // on show, put the dialog in the right place
                    YAHOO.util.Event.on('addTagTrigger', 'click', function() {
                        window.location = "${createLink(controller:'user', action:'login', params:[originalURI:request.forwardURI])}";
                    });
                    // also hang up rating click if not logged in, redirect to login page with originalURI of this page
                    // for redirect
                    YAHOO.util.Event.on('ratingdiv', 'click', function(e) {
                        YAHOO.util.Event.stopEvent(e);
                        window.location = "${createLink(controller:'user', action:'login', params:[originalURI:request.forwardURI])}";
                    });
                });
            </script>
        </jsec:isNotLoggedIn>

        <br/><br/>

        <cache:text id="pluginTabs_${plugin.id}">
            <gui:tabView>
                <g:each var="wiki" in="${Plugin.WIKIS}">
                    <gui:tab id="${wiki}Tab" label="${wiki[0].toUpperCase() + wiki[1..-1]}" active="${wiki == 'description'}">
                        <g:render template="../content/viewActions" model="${[content: plugin[wiki], update: wiki + 'Tab', editFormName: wiki + 'EditForm']}"/>
                        <div class='${wiki}, wikiPage'><wiki:text page="${plugin[wiki]?.title}" /></div>
                    </gui:tab>
                </g:each>
            </gui:tabView>
		</cache:text>


        <g:render template="../content/previewPane"/>
        
        <g:render template="../comments/comments" model="${[commentType:'plugin', parentId:plugin.id, comments:plugin.comments]}"/>

    </div>
</div>
</body>
</html>