<html>
<head>
  <title><g:layoutTitle default="DB Util Plugin"/></title>
  <link rel="stylesheet" href="${createLinkTo(dir: pluginContextPath, file: 'css/dbUtil.css')}"/>
  <link rel="shortcut icon" href="${createLinkTo(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
  <g:layoutHead/>
  <g:javascript library="application"/>
</head>
<body>
<div id="main-content">
  <div id="inside">
      <div id="header">
        <g:link controller="dbUtil" action="data">Display Data</g:link>
        <g:link controller="dbUtil" action="info">Database Info</g:link>
        <g:link controller="dbUtil" action="sql">Execute SQL</g:link>
      </div>
      <g:layoutBody/>
  </div>
</div>
</body>
</html>
