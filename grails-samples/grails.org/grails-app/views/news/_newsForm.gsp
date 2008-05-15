<div id="editPane">
    <h1>Create News</h1>

    <g:hasErrors bean="${newsItem}">
        <div id="errors" class="errors">
            <g:renderErrors bean="${newsItem}"></g:renderErrors>
        </div>
    </g:hasErrors>

    <g:form name="createNews" url="[controller:'news', action:'createNews']">
        Enter Title:&nbsp;&nbsp;&nbsp;<g:textField id="title" name="title" value="${newsItem?.title}" /> <br /><br/>
        Enter Body (300 characters max, wiki text supported)<br />
        <g:textArea id="body" name="body" rows="20" cols="75" value="${newsItem?.body}" /> <br />
        <g:submitButton name="save" value="Save" />
    </g:form>
</div>
<script type="text/javascript">
    if($("errors")!=null) {
        Effect.Fade("errors", {delay:3})
    }
</script>