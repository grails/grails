<g:applyLayout name="storeLayout">
	<div id="invoice" class="shoppingCart">
		<h2>Your Receipt</h2>
		<p>Congratulations you have completed your purchase. Those purchases that included shipping
			will ship within 2-3 working days. Your digital purchases have been transferred into your library</p>
		<p>Your invoice number is ${payment.invoiceNumber}</p>
		<h2>Purchased Items</h2>

			<ul>
				<g:each in="${albumPayments}" var="albumPayment">
					<li>${albumPayment.album.artist.name} - ${albumPayment.album.title}<br>
						<strong>Cost: </strong> $ 	${albumPayment.album.price}
						</li>
				</g:each>
				
			</ul>
		<p><strong>Total: </strong> ${albumPayments.album.price.sum()}</p>		
	</div>	
</g:applyLayout>
