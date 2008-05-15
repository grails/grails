<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>${content?.title}</title>
    <meta content="subpage" name="layout" />
    <g:javascript library="scriptaculous" />
    <g:javascript library="diff_match_patch" />

    <style type="text/css">

        .previewPane {

            z-index:999;
            position:absolute;
            background:white;
            border:2px solid black;
            top:100px;
            width:600px;
            height:500px;
            overflow-y:auto;
            overflow-x:hidden;
        }
    </style>

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

        function getAjaxOptions(after) {
            if(after ==null)  {
                after = function() {}
            }
            return {asynchronous:true,
                evalScripts:true,
                parameters:Form.serialize($('wikiEditForm')),
                method:"POST",
                onComplete:after
            }
        }

        function hidePreview() {           
           Effect.Fade('previewContainer')
        }
        function showPreview() {
           Effect.Appear('previewContainer')
        }
    </script>
</head>
<body>
    <div id="contentPane">
        <g:render template="viewActions" model="[content:content]" />
        <div id="editPane" style="margin-top:10px;">
            <wiki:text key="${content?.title}">
                ${content?.body}
            </wiki:text>
        </div>
    </div>
    <div id="previewContainer" class="previewPane" style="display:none;">
        <div style="padding:5px;margin-left:530px;"><a href="#" onclick="hidePreview();">Close</a> </div>
        <div id="previewPane" style="margin:10px; ">

        </div>
    </div>
</body>
</html>