<html>
  <head>
      <gui:resources components='expandablePanel'/>
	  <title>Grails.org Error</title>
      <meta content="subpage" name="layout" />
	  <style type="text/css">
	body {
	    font-family: Lucida Grande, Lucida, sans-serif;
	    font-size: 12pt;
	}
	
	  		.message {
	  			border: 1px solid black;
	  			padding: 5px;
	  			background-color:#E9E9E9;
	  		}
	  		.stack {
	  			border: 1px solid black;
	  			padding: 5px;	  		
	  			overflow:auto;
	  			height: 300px;
	  		}
	  		.snippet {
	  			padding: 5px;
	  			background-color:white;
	  			border:1px solid black;
	  			margin:3px;
	  			font-family:courier;
	  		}
	  </style>
  </head>
  
  <body>
    <h1>An Error has occurred</h1>
    <p>We're sorry, but there has been a problem rendering the page you've requested.  This incident has been logged, and will be looked into soon.</p>

  <div class='yui-skin-sam'>
      <gui:expandablePanel title="Error Details" bounce="false">
          <div class="message">
              <strong>Message:</strong> ${exception.message?.encodeAsHTML()} <br />
              <strong>Caused by:</strong> ${exception.cause?.message?.encodeAsHTML()} <br />
              <strong>Class:</strong> ${exception.className} <br />
              <strong>At Line:</strong> [${exception.lineNumber}] <br />
              <strong>Code Snippet:</strong><br />
              <div class="snippet">
                  <g:each var="cs" in="${exception.codeSnippet}">
                      ${cs?.encodeAsHTML()}<br />
                  </g:each>
              </div>
          </div>
          <h2>Stack Trace</h2>
          <div class="stack">
              <pre><g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}<br/></g:each></pre>
          </div>
      </gui:expandablePanel>
  </div>
  </body>
</html>