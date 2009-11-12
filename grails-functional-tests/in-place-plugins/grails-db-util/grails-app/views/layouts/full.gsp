<html>
<head>
  <title><g:layoutTitle default="DB Util Plugin"/></title>
  <link rel="stylesheet" href="${resource(dir: '', file: 'css/dbUtil.css', plugin: 'db-util')}"/>
  <link rel="stylesheet" href="${resource(dir: '', file: 'css/standard.css')}"/>
  <link rel="stylesheet" href="${resource(dir: pluginContextPath, file: 'css/oldstyle.css')}"/>
  <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico', plugin: 'db-util')}" type="image/x-icon"/>
  <g:javascript src="dojo.js"/>
  <g:javascript library="application"/>
  <g:layoutHead/>
</head>
<body>
<div id="main-content">
  <div id="inside">
      <div id="header">
        <g:link controller="dbUtil" action="data">Display Data</g:link>
        <g:link controller="dbUtil" action="info">Database Info</g:link>
        <g:link controller="dbUtil" action="sql">Execute SQL</g:link>
        <span id="pluginContext">${pluginContextPath}</span>
      </div>
      <g:layoutBody/>
  </div>
</div>
</body>
</html>
