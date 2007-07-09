                                                                        
<html>
    <head>
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
         <meta name="layout" content="main" />
         <title>Your Shopping Cart</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a href="${createLinkTo(dir:'')}">Home</a></span>
        </div>
        <div class="body">
           <h1>Your Shopping Cart</h1>
            <g:if test="${flash.message}">
                 <div class="message">
                       ${flash.message}
                 </div>
            </g:if>    
			<h2>Cart Items</h2>
			<g:form action="shoppingCart">
				      <table>
	             <thead>
	               <tr>

	                   	    <g:sortableColumn property="id" title="Id" />

	                   	    <g:sortableColumn property="title" title="Title" />

	                   	    <g:sortableColumn property="author" title="Author" />

	                   	    <g:sortableColumn property="price" title="Price" />

	                        <th></th>
	               </tr>
	             </thead>
	             <tbody>
	               <g:each in="${cartItems}">
	                    <tr>

	                            <td>${it.id?.encodeAsHTML()}</td>

	                            <td>${it.title?.encodeAsHTML()}</td>

	                            <td>${it.author?.encodeAsHTML()}</td>

	                            <td>${it.price?.encodeAsHTML()}</td>

	                       <td class="actionButtons">
	                       </td>
	                    </tr>
	               </g:each>   
					<tr>
						<td></td>
						<td></td>
						<td></td>
						<td>Total: ${cartItems.price.sum()}</td>
						<td></td>
					</tr>
	             </tbody>
	           </table>
				<g:submitButton name="continueShopping" value="Continue Shopping"></g:submitButton>	           
				<g:submitButton name="checkout" value="Checkout"></g:submitButton>
			</g:form>

        </div>
    </body>
</html>
