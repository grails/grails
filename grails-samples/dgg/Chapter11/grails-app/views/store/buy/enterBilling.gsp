<g:applyLayout name="storeLayout">
	<div id="shoppingCart" class="shoppingCart">
		<h2>Enter your billing details below:</h2>
		<div id="shippingForm" class="formDialog">
			<g:form name="shippingForm"  url="[controller:'store',action:'buy']">
			

					<g:submitButton name="back" value="Back"></g:submitButton>
					<g:submitButton name="confirm" value="Next"></g:submitButton>			

			</g:form>
		</div>
	</div>	
</g:applyLayout>
