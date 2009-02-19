<%@ page import="org.grails.plugin.Plugin" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <gui:resources components="['tabView','dialog']"/>
    <g:javascript library="prototype" />
    <yui:javascript dir='animation' file='animation-min.js'/>
    <script src="${createLinkTo(dir:'js', file:'rating.js')}"></script>
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'plugins.css')}" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'comments.css')}" />
    <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'ratings.css')}" />
    <title>${plugin.title} Plugin</title>
    <meta content="subpage" name="layout"/>
</head>
<body>
<div id="contentPane">
    <div id="infoLinks" style="margin-left:520px;">
        <g:link controller="plugin" action="list">All Plugins</g:link><br/>
        <g:link controller="plugin" action="editPlugin" params="${[name:plugin.name]}"><img src="${createLinkTo(dir: 'images/', 'icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0"/>Edit Plugin</g:link>
    </div>

    <gui:dialog id='loginDialog' title="Login required" modal="true">
        <div id='loginFormDiv'></div>
    </gui:dialog>

    <h1>${plugin?.title}</h1>

    <%
        def officialStyle = plugin.official ? 'official' : ''
    %>
    <div class="plugin ${officialStyle}">

        <table class='details'>
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
        </table>

        <br/><br/>


        <gui:tabView>
            <g:each var="wiki" in="${Plugin.WIKIS}">
                <gui:tab label="${wiki[0].toUpperCase() + wiki[1..-1]}" active="${wiki == 'description'}">
                    <div class='${wiki}, wikiPage'><wiki:text>${plugin."$wiki"}</wiki:text></div>
                </gui:tab>
            </g:each>
        </gui:tabView>
    
        <g:render template="/comment/comments" model="${[parentId:plugin.id, comments:comments]}"/>

    </div>
</div>
</body>
</html>