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
				def items = ctx.flowScope.get("cartItems")
				if(!items) items = [] as HashSet
				items << Book.get(params.id)
				ctx.flowScope.put("cartItems", items)
			}.to "showCart"
		}       
		showCart {
			on("checkout").to "enterPersonalDetails"
			on("continueShopping").to "showCatalogue"
		}
		enterPersonalDetails {
			on("submit") { ctx ->
				def p = new Person(params)
				ctx.flowScope.put("person",p)  
				if(p.hasErrors() || !p.validate()) {
				  return error()
				} 
			}.to "enterShipping"    
			on("return").to "showCart" 
			on(Exception).to "handleError"
		}               
		enterShipping  {
			on("back").to "enterPersonalDetails"
			on("submit") { ctx ->
				def a = new Address(params)
				ctx.flowScope.put("address",a)
				if(a.hasErrors() || !a.validate()) {
				  return error()
				}
			}.to "enterPayment"
		}                                
		enterPayment  {
			on("back").to "enterShipping"
			on("submit") { ctx ->
				def pd = new PaymentDetails(params)
				ctx.flowScope.put("paymentDetails",pd)
				if(pd.hasErrors() || !pd.validate()) return error()
			}.to "confirmPurchase"
		}                                   
		confirmPurchase  {   
			on("back").to "enterPayment"
			on("confirm").to "processPurchaseOrder"
		}                                         
		processPurchaseOrder  {
			action { ctx ->                 
				def a =  ctx.flowScope.remove("address")
				def p = ctx.flowScope.remove("person")
				def pd = ctx.flowScope.remove("paymentDetails")
				def o = new Order(person:p, shippingAddress:a, paymentDetails:pd)
				o.invoiceNumber = new Random().nextInt(99999)
				def cartItems = ctx.flowScope.remove("cartItems")
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
