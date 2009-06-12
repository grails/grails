import org.springframework.context.*

class BlahController implements ApplicationContextAware {
	
	ApplicationContext applicationContext
	

    AaaaService aaaaService
    BbbbService bbbbService
	JavaService javaService
	
	def java = {

		javaService.callMe()
		render "success"
	}

	
	def index = {
        bbbbService.serviceMethod()		
		aaaaService.serviceMethod()
		render "success"
	}
}
