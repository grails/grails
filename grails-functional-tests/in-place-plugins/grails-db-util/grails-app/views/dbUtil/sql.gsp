<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="full"/>
  <title>Execute SQL</title>
</head>
<body>
<div class="body">
  <h1>Execute SQL</h1>
  <g:form action="sql" method="post">
    <div><g:textArea style="width:95%" id="sqlText" cols="1000" rows="10" name='sqlText' value='${sqlText}'/></div>
    <div><g:submitButton name="run" value='Execute'/></div>
  </g:form>
  <table border="1">

    <g:each in="${dataList}" status='i' var="row">
      <g:if test='${i==0}'>
        <tr>
          <g:each in="${row}" var="col">
            <th>
              ${col.key}
            </th>
          </g:each>
        </tr>
      </g:if>
      <tr>
        <g:each in="${row}" var="col">
          <td>
            ${col.value}
          </td>
        </g:each>
      </tr>
    </g:each>
  </table>
</div>
</body>
</html>
