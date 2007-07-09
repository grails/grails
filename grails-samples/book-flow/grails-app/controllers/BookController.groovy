class BookController {   
	def index = {
		redirect(action:'shoppingCart')
	}    
	def shoppingCartFlow = {   
		getBooks {
			action { [ bookList:Book.list() ]}   
			on("success").to "showCatalogue"
			on(Exception).to "handleError"
		}
		showCatalogue {
			on("chooseBook") { ctx ->
				if(!params.id)return error()
				def items = ctx.flow.cartItems
				if(!items) items = [] as HashSet
				items << Book.get(params.id)
				ctx.flow.cartItems = items
			}.to "showCart"
		}       
		showCart {
			on("checkout").to "enterPersonalDetails"
			on("continueShopping").to "showCatalogue"
		}
		enterPersonalDetails {
			on("submit") { ctx ->
				def p = new Person(params)                      
				ctx.flow.person = p
				if(p.hasErrors() || !p.validate())return error()				
			}.to "enterShipping"    
			on("return").to "showCart" 
			on(Exception).to "handleError"
		}               
		enterShipping  {
			on("back").to "enterPersonalDetails"
			on("submit") { ctx ->
				def a = new Address(params)				                        
				ctx.flow.address = a
				if(a.hasErrors() || !a.validate()) return error()				
			}.to "enterPayment"
		}                                
		enterPayment  {
			on("back").to "enterShipping"
			on("submit") { ctx ->
				def pd = new PaymentDetails(params)                
				ctx.flow.paymentDetails = pd
				if(pd.hasErrors() || !pd.validate()) return error()
			}.to "confirmPurchase"
		}                                   
		confirmPurchase  {   
			on("back").to "enterPayment"
			on("confirm").to "processPurchaseOrder"
		}                                         
		processPurchaseOrder  {
			action { ctx ->                 
				def a =  ctx.flow.address
				def p = ctx.flow.person
				def pd = ctx.flow.paymentDetails
				def cartItems = ctx.flow.cartItems
				ctx.flow.clear()
				
				def o = new Order(person:p, shippingAddress:a, paymentDetails:pd)
				o.invoiceNumber = new Random().nextInt(9999999)								
				cartItems.each { o.addToItems(it) }
				[order:o]
			}   
			on("error").to "confirmPurchase"
			on(Exception).to "confirmPurchase"
			on("success").to "displayInvoice"
		}                                   
		displayInvoice() 
		handleError()
	}
}     
