<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="full"/>
    <title>Database Dump</title>
    <link rel="stylesheet" href="${resource(dir: '', file: 'css/data.css')}"/>
    <g:javascript src="dojo-anim.js"/>
</head>
<body>

<div class="body">
    <h1>Display Data</h1>
    <!--Tables-->
    <g:each var="tableName" in="${tableList.sort()}">
        <br/>
        <b>${tableName}</b>
        <table>
            <thead>
                <!--Columns-->
                <tr>
                    <g:each var="col" in="${columnList[tableName].sort()}">
                        <th title="${col}">${col}</th>
                    </g:each>
                </tr>
            </thead>
            <tbody>
                <!--Data-->
                <g:each in="${dataList[tableName]}" var="row">
                    <tr>
                        <g:each in="${row}" var="field">
                            <td>${field?.encodeAsHTML()}</td>
                        </g:each>
                    </tr>
                </g:each>
            </tbody>
        </table>
    </g:each>
</div>
</body>
</html>
