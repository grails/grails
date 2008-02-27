<%--
  Created by IntelliJ IDEA.
  User: graeme
  Date: Feb 19, 2008
  Time: 11:42:46 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
      <title>Login Page</title>
      <meta content="main" name="layout" />
  </head>
  <body>
        <g:render template="loginForm" model="${pageScope.variables}" />
  </body>
</html>