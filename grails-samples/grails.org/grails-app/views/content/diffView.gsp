<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>${content?.title}</title>
    <meta content="subpage" name="layout" />
    <g:javascript library="scriptaculous" />
    <g:javascript library="diff_match_patch" />

    <script type="text/javascript">
        var dmp = new diff_match_patch();

        function showDiff() {


            var text1 = $("text1").innerHTML
            var text2 = $("text2").innerHTML

            var d = dmp.diff_main(text1, text2);
            dmp.diff_cleanupSemantic(d);
            var ds = dmp.diff_prettyHtml(d);

            $('diffOutputDiv').innerHTML = ds;

            Effect.Appear('diffOutputDiv')
        }

    </script>
</head>
<body>
    <div id="contentPane">
        <div id="infoLinks" style="margin-left:520px;">
            <g:link controller="content" id="${content?.title}"><img src="${createLinkTo(dir:'images/','icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0" /></g:link>
            <g:link update="contentPane" controller="content" id="${content?.title}">View Page</g:link>
        </div>
        
        <g:render template="/common/messages" model="${pageScope.getVariables()}" />

        <div style="display:none;">
            <div id="text1">${text1}</div>
            <div id="text2">${text2}</div>
        </div>

        <div id="diffOutputDiv"></div>

        <g:render template="/common/messages_effects" model="${pageScope.getVariables()}" />
        <script type="text/javascript">
            showDiff()
        </script>
    </div>
</body>
</html>