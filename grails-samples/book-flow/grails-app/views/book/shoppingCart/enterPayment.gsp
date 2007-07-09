                                                                                                 
<html>
    <head>
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
         <meta name="layout" content="main" />
         <title>Enter Payment Details</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
        </div>
        <div class="body">
           <h1>Enter Payment Details</h1>
           <g:if test="${flash.message}">
                 <div class="message">${flash.message}</div>
           </g:if>
           <g:hasErrors bean="${paymentDetails}">
                <div class="errors">
                    <g:renderErrors bean="${paymentDetails}" as="list" />
                </div>
           </g:hasErrors>
           <g:form action="shoppingCart" method="post" >
               <div class="dialog">
                <table>
                    <tbody>

                       
                       
                                  <tr class='prop'><td valign='top' class='name'><label for='name'>Card Number:</label></td><td valign='top' class='value ${hasErrors(bean:paymentDetails,field:'cardNumber','errors')}'><input type="text" name='cardNumber' value="${paymentDetails?.cardNumber}"/></td></tr>
                                  <tr class='prop'><td valign='top' class='name'><label for='name'>Expiry Date:</label></td><td valign='top' class='value ${hasErrors(bean:paymentDetails,field:'expiryDate','errors')}'><input type="text" name='expiryDate' value="${paymentDetails?.expiryDate}"/></td></tr>
                       
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
