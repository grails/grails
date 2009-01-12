<g:applyLayout name="storeLayout">
	<div id="shoppingCart" class="shoppingCart">
		<h2>Enter your shipping details below:</h2>
		<div id="shippingForm" class="formDialog">
			<g:hasErrors bean="${shippingAddress}">
				<div class="errors">
					<g:renderErrors bean="${shippingAddress}"></g:renderErrors>				
				</div>
				
			</g:hasErrors>

			<g:form name="shippingForm"  url="[controller:'store',action:'buy']">
					<div class="formFields">
						<div>
							<label for="number">House Name/Number:</label><br><g:textField name="number" value="${shippingAddress?.number}"></g:textField>						
						</div>
						<div>
							<label for="street">Street:</label><br><g:textField name="street" value="${shippingAddress?.street}"></g:textField>						
						</div>

						<div>
							<label for="postCode">Post Code:</label><br><g:textField name="postCode" value="${shippingAddress?.postCode}"></g:textField>									
						</div>

						<div>
							<label for="city">City:</label><br><g:textField name="city" value="${shippingAddress?.city}"></g:textField>									
						</div>
						<div>
							<label for="state">State/County:</label><br><g:textField name="state" value="${shippingAddress?.state}"></g:textField>									
						</div>
						<div>
							<label for="country">Country:</label><br><g:textField name="country" value="${shippingAddress?.country}"></g:textField>									
						</div>
						
					</div>
					
					<div class="formButtons">
						<g:submitButton type="image" src="${createLinkTo(dir:'images',file:'back-button.gif')}" border="0" name="back" value="Back"></g:submitButton>
						<g:submitButton type="image" src="${createLinkTo(dir:'images',file:'next-button.gif')}" name="next" value="Next"></g:submitButton>									
					</div>


			</g:form>
		</div>
	</div>	
</g:applyLayout>
