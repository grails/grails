<g:applyLayout name="storeLayout">
	<div id="shoppingCart" class="shoppingCart">
		<h2>Your Purchase</h2>
		<p>You have the following items in your cart that you wish to Purchase. </p>

		<ul>
			<g:each in="${albumPayments}" var="albumPayment">

				<li>${albumPayment.album.artist.name} - ${albumPayment.album.title}<br>
					<strong>Cost: </strong> $ 	${albumPayment.album.price}
					</li>
			</g:each>
			
		</ul>
		<g:set var="totalAmount"><g:formatNumber number="${albumPayments.album.price.sum()}" 
												 format="0.00" /></g:set>
		<p><strong>Total:</strong> $ ${totalAmount}</p>

		
		<h2>Card Details</h2>
		<p>The following card details will be used to process this transaction:</p>
		<div class="cardDetails">
			<ul>
				<li><strong>Name:</strong> ${creditCard?.name}</li>
				<li><strong>Number:</strong> ${creditCard?.number}</li>
				<li><strong>Expiry:</strong> ${creditCard?.expiry}</li>
				<li><strong>Security Code:</strong> ${creditCard?.code}</li>									
			</ul>
			
		</div>
		<div class="formButtons">
			<g:link controller="store" action="buy" event="back">
			    <img src="${createLinkTo(dir:'images',file:'back-button.gif')}" 
			         border="0">
			</g:link>
			<g:link controller="store" action="buy" event="confirm">
			      <img src="${createLinkTo(dir:'images',file:'confirm-button.gif')}" 
			       border="0">
			</g:link>									
		</div>
	</div>		
</g:applyLayout>
