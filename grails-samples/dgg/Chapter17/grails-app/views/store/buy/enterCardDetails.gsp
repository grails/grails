<g:applyLayout name="storeLayout">
	<div id="shoppingCart" class="shoppingCart">
		<h2>Enter your credit card details below:</h2>
		<div id="shippingForm" class="formDialog">
			<g:hasErrors bean="${creditCard}">
				<div class="errors">
					<g:renderErrors bean="${creditCard}"></g:renderErrors>
				</div>
			</g:hasErrors>
			<g:form name="shippingForm"  url="[controller:'store',action:'buy']">
					<div class="formFields">
						<div>
							<label for="name">Name on Card:</label><br>
							<g:textField name="name" value="${fieldValue(bean:creditCard,field:'name')}" />
						</div>
						<div>
							<label for="number">Card Number:</label><br>
							<g:textField name="number" value="${fieldValue(bean:creditCard,field:'number')}" />
						</div>

						<div>
							<label for="expiry">Expiry (eg. 05/10):</label><br>
							<g:textField name="expiry" value="${fieldValue(bean:creditCard,field:'expiry')}" />
						</div>

						<div>
							<label for="code">Security Code:</label><br>
							<g:textField name="code" value="${fieldValue(bean:creditCard,field:'code')}" />
						</div>


					</div>					

					<div class="formButtons">
						<g:submitButton class="blueButton" 
						                type="image" 
						                src="${createLinkTo(dir:'images',file:'back-button.gif')}" 
						                border="0" 
						                name="back" 
						                value="Back" />
						<g:submitButton class="blueButton" 
						                type="image" 
						                src="${createLinkTo(dir:'images',file:'next-button.gif')}" 
						                name="next" 
						                value="Next"></g:submitButton>									
					</div>
		

			</g:form>
		</div>
	</div>	
</g:applyLayout>
