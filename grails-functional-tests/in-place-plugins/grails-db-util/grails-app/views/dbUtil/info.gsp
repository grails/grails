<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>Database Info</title>
    <link rel="stylesheet" href="${resource(dir:'css',file:'other.css', plugin: 'db-util')}" />
    <g:javascript src="prototype/scriptaculous.js" />
</head>
<body>
<div class="body">
    <h1>Database Info</h1>
    <table>
        <thead>
            <tr>
                <th title="Schema"/>
                <th title="Table"/>
                <th title="Column"/>
                <th title="Parms"/>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>${schemaList[0].encodeAsHTML()}</td>
            </tr>
            <!--Tables-->
            <g:each in="${tableList.sort()}" status="i" var="table">
                <tr>
                    <td>&nbsp;</td>
                    <td>${table.encodeAsHTML()}</td>
                </tr>
                <!--Columns-->
                <g:each in="${columnList[table]}" status="j" var="column">
                    <tr>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>${column.key?.encodeAsHTML()}</td>
                        <td>${column.value?.encodeAsHTML()}</td>
                    </tr>
                </g:each>
            </g:each>
        </tbody>
    </table>
</div>
</body>
</html>
