<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
      <title>Upload dialog</title>
      <g:javascript library="scriptaculous" />
      <link rel="stylesheet" href="${createLinkTo(dir:'css', file:'master.css')}" type="text/css" media="screen" title="Master screen stylesheet" charset="utf-8" />

      <style type="text/css">
        body {
            background:none;
            text-align: left;
        }

      </style>
  </head>

  <body>
      <div id="uploadDialogDiv" >
          <g:render template="/common/messages" model="${pageScope.getVariables()}" />
          <g:uploadForm name="uploadForm" url="[controller:'content', action:'uploadImage', id:category]" onsubmit="\$('progressDiv').show();\$('uploadDialogDiv').hide()">
              <g:field type="file" name="file" />
              <g:submitButton name="upload" value="upload" />
          </g:uploadForm>
      </div>
      <div id="progressDiv" style="display:none;">
          <img src="${createLinkTo(dir: 'images', file: 'spinner.gif')}" alt="Please wait.." /> Please wait...
      </div>
      <g:render template="/common/messages_effects" model="${pageScope.getVariables()}" />
  </body>
</html>