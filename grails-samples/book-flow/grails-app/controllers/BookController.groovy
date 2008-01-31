import org.springframework.context.*

class BookController implements ApplicationContextAware  {   
	ApplicationContext applicationContext
	
	def test = {  
		GroovySystem.metaClassRegistry.removeMetaClass(String)   	           
		render "${applicationContext.getBeanDefinition('bookService')}"
	}                                                                  
	
	def index = {
		redirect(action:'shoppingCart')
	}    
	def shoppingCartFlow = {   
		getBooks {
			action { 

				[ bookList:Book.list() ]
			}   
			on("success").to "showCatalogue"
			on(Exception).to "handleError"
		}
		showCatalogue { 
			on("chooseBook") { 
				if(!params.id)return error()
				def items = flow.cartItems
				if(!items) items = [] as HashSet
				items << Book.get(params.id)
				flow.cartItems = items
			}.to "showCart"
		}       
		showCart {
	    	on("checkout").to "enterPersonalDetails"
		    on("continueShopping").to "showCatalogue"
		}                                          
		enterPersonalDetails {
			on("submit") { 
				def p = new Person(params)
				flow.person = p  
				def e = yes()
				if(p.hasErrors() || !p.validate())return error()				
			}.to "enterShipping"    
			on("return").to "showCart" 
			on(Exception).to "handleError"
		}               
		enterShipping  {
			on("back").to "enterPersonalDetails"
			on("submit") { 
				def a = new Address(params)				                        
				flow.address = a
				if(a.hasErrors() || !a.validate()) return error()				
			}.to "enterPayment"
		}                                
		enterPayment  {
			on("back").to "enterShipping"
			on("submit") { 
				def pd = new PaymentDetails(params)                
				flow.paymentDetails = pd
				if(pd.hasErrors() || !pd.validate()) return error()
			}.to "confirmPurchase"
		}                                   
		confirmPurchase  {   
			on("back").to "enterPayment"
			on("confirm").to "processPurchaseOrder"
		}                                         
		processPurchaseOrder  {
			action {                  
				def a =  flow.address
				def p = flow.person
				def pd = flow.paymentDetails
				def cartItems = flow.cartItems
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
