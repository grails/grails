<%@ page import="org.grails.plugin.Plugin" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <gui:resources components="['tabView','dialog','autoComplete']" javascript='animation'/>
    <g:javascript library="diff_match_patch" />
    <g:javascript library="scriptaculous" />
    <script src="${createLinkTo(dir:'js', file:'rating.js')}"></script>
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'content.css')}" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'plugins.css')}" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'comments.css')}" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'ratings.css')}" />
    <title>${plugin.title} Plugin</title>
    <meta content="subpage" name="layout"/>
    <g:render template="../content/wikiJavaScript"/>    
</head>
<body>
<%
    def officialStyle = plugin.official ? 'official' : ''
%>
<div id="contentPane" class='${officialStyle}'>
    <div id="infoLinks" style="margin-left:520px;">
        <g:link class="home" controller="plugin" action="index">Plugins Home</g:link><br/>
        <g:link controller="plugin" action="editPlugin" id="${plugin.id}"><img src="${createLinkTo(dir: 'images/', 'icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0"/>Edit Plugin</g:link>
    </div>

    <gui:dialog id='loginDialog' title="Login required" modal="true">
        <div id='loginFormDiv'></div>
    </gui:dialog>

    <h1>${plugin?.title}</h1>

    <div class="plugin">

        <table class='details ${officialStyle}'>
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
                <td colspan='2'><a href="${plugin.documentationUrl}">Official Docs</a></td>
            </tr>
            <tr>
                <th>Built on Grails</th>
                <td>${plugin.grailsVersion ?: '?'}</td>
                <td colspan='2'><a href="${plugin.downloadUrl}">Download</a></td>
            </tr>
            <tr>
                <th>Rating</th>
                <td colspan='3'>
                    <g:render template="ratings" model="[parentId:plugin.id, average:plugin.avgRating, total:plugin.ratings.size(), active: jsec.principal() && !(userRating as Boolean)]"/>
                </td>
            </tr>
            <tr>
                <th>Tags</th>
                <td colspan='3'>
                    <span id='pluginTags'>
                        <g:render template='tags' var='plugin' bean="${plugin}"/>
                    </span>
                    <jsec:isLoggedIn><span id='addTagTrigger' ><img src="${createLinkTo(dir: 'images/famfamfam', file: 'add.png')}"/></span></jsec:isLoggedIn>
                </td>
            </tr>
        </table>

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
        </jsec:isLoggedIn>

        <br/><br/>

        <gui:tabView>
            <g:each var="wiki" in="${Plugin.WIKIS}">
                <gui:tab id="${wiki}Tab" label="${wiki[0].toUpperCase() + wiki[1..-1]}" active="${wiki == 'description'}">
                    <g:render template="../content/viewActions" model="${[content: plugin[wiki], update: wiki + 'Tab']}"/>
                    <div class='${wiki}, wikiPage'><wiki:text>${plugin."$wiki"}</wiki:text></div>
                </gui:tab>
            </g:each>
        </gui:tabView>

        <g:render template="../content/previewPane"/>
        
        <g:render template="../comment/comments" model="${[commentType:'plugin', parentId:plugin.id, comments:comments]}"/>

    </div>
</div>
</body>
</html>