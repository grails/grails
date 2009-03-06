<script type="text/javascript">
    var dmp = new diff_match_patch();

    function hideCommentPost() {
        YAHOO.util.Dom.addClass('postComment', 'hidden');
    }
    function showCommentPost() {
        YAHOO.util.Dom.removeClass('postComment', 'hidden');
    }

    function showDiff() {


        var text1 = $("text1").innerHTML
        var text2 = $("text2").innerHTML

        var d = dmp.diff_main(text1, text2);
        dmp.diff_cleanupSemantic(d);
        var ds = dmp.diff_prettyHtml(d);

        $('diffOutputDiv').innerHTML = ds;

        Effect.Appear('diffOutputDiv')
    }

    function getAjaxOptions(after, editFormName) {
        if (after == null) {
            after = function() {
            }
        }
        if (editFormName == null) {
            editFormName = 'wikiEditForm'
        }
        return {asynchronous:true,
            evalScripts:true,
            parameters:Form.serialize($(editFormName)),
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