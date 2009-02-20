<%@ page import="org.grails.plugin.Plugin" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <gui:resources components="['tabView','dialog','autoComplete']"/>
    %{--<g:javascript library="prototype" />--}%
    <g:javascript library="diff_match_patch" />
    <g:javascript library="scriptaculous" />
    <yui:javascript dir='animation' file='animation-min.js'/>
    <script src="${createLinkTo(dir:'js', file:'rating.js')}"></script>
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
        <g:link controller="plugin" action="list">All Plugins</g:link><br/>
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
                <td colspan='2'>${plugin.authorEmail}</td>
            </tr>
            <tr>
                <th>Docs</th>
                <td colspan='3'><a href="${plugin.documentationUrl}">${plugin.documentationUrl}</a></td>
            </tr>
            <tr>
                <th>Download</th>
                <td colspan='3'><a href="${plugin.downloadUrl}">${plugin.downloadUrl}</a></td>
            </tr>
            <tr>
                <th>Current Release</th>
                <td>${plugin.currentRelease}</td>
                <th>Built on Grails</th>
                <td>${plugin.grailsVersion}</td>
            </tr>
            <tr>
                <th>Rating</th>
                <td colspan='3'>
                    <g:render template="ratings" model="[parentId:plugin.id, average:plugin.avgRating]"/>
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
        
        <g:render template="/comment/comments" model="${[parentId:plugin.id, comments:comments]}"/>

    </div>
</div>
</body>
</html>