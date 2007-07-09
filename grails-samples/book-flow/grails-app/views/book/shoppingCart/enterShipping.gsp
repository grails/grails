                                            
<html>
    <head>
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
         <meta name="layout" content="main" />
         <title>Enter Shipping Details</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
        </div>
        <div class="body">
           <h1>Enter Shipping Details</h1>
           <g:if test="${flash.message}">
                 <div class="message">${flash.message}</div>
           </g:if>
			<g:hasErrors bean="${address}">
	            <div class="errors">
	                <g:renderErrors bean="${address}" as="list" />
	            </div>				
			</g:hasErrors>           <g:form action="shoppingCart" method="post" >
               <div class="dialog">
                <table>
                    <tbody>

                       
                       
                                  <tr class='prop'><td valign='top' class='name'><label for='name'>House Name/Number:</label></td><td valign='top' class='value ${hasErrors(bean:address,field:'number','errors')}'><input type="text" name='number' value="${address?.number?.encodeAsHTML()}"/></td></tr>
                                  <tr class='prop'><td valign='top' class='name'><label for='name'>Post Code:</label></td><td valign='top' class='value ${hasErrors(bean:address,field:'postCode','errors')}'><input type="text" name='postCode' value="${address?.postCode?.encodeAsHTML()}"/></td></tr>
                       
                    </tbody>
               </table>
               </div>
               <div class="buttons">
                     <span class="formButton">
						<g:submitButton name="back" value="Back"></g:submitButton>								
						<g:submitButton name="submit" value="Next"></g:submitButton>
                     </span>
               </div>
            </g:form>
        </div>
    </body>
</html>
