<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
          <!--[if IE]>
            <link rel="stylesheet" href="${createLinkTo(dir:'css/ie',file:'master.css')}" />
          <![endif]--> 
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />				
    </head>
    <body class='yui-skin-sam'>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${createLinkTo(dir:'images',file:'spinner.gif')}" alt="Spinner" />
        </div>	
        <div class="logo"><img src="${createLinkTo(dir:'images',file:'grails_logo.jpg')}" alt="Grails" /></div>	
        <g:layoutBody />		
    </body>	
</html>