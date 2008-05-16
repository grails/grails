
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
      <title>Login Page</title>
      <meta content="subpage" name="layout" />
      <g:javascript library="scriptaculous" />
      <style type="text/css">
          #content-container {
              height:700px;
          }

      </style>
      
  </head>
  <body>
        <div id="contentPane">
            <g:render template="loginForm" model="${pageScope.variables}" />
        </div>
  </body>
</html>