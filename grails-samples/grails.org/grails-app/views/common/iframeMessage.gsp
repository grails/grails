<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
      <title>Message</title>
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
    <div id="body">
        <div id="message" class="message">
            ${message}
        </div>
        <script type="text/javascript">
        if($("message")!=null) {
        Effect.Fade("message",
            {   delay:7,
                afterFinish:function() {
                                 var dialog = window.parent.$('${pageId}Dialog')
                                 var iframe = window.parent.$('${pageId}Iframe')
                                 if(dialog!=null) {
                                     dialog.hide()
                                     <g:if test="${frameSrc}">
                                         if(iframe!=null)
                                            iframe.src = "${frameSrc.encodeAsJavaScript()}"
                                     </g:if>
                                 }
                            }
            })
        }
        </script>
    </div>
  </body>
</html>