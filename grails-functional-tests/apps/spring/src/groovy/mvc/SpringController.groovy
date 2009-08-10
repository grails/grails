package mvc

import org.springframework.stereotype.*
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.*
import org.hibernate.*
import org.springframework.ui.*

@Controller
class SpringController {

	@Autowired
	SessionFactory sessionFactory
	
	@RequestMapping("/hello.dispatch")
	ModelMap handleRequest() {
		def session = sessionFactory.getCurrentSession()
		return new ModelMap(session.get(Person, 1L))		
	}

}