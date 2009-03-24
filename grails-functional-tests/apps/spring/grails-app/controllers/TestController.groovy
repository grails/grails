import org.springframework.context.*

class TestController implements ApplicationContextAware {
	
	ApplicationContext applicationContext

	def localeResolver
	
	def testOverridenBean = {
		render "Resolver class = ${localeResolver.class.name}"
	}
	
	def messageSource
	def testOverridenBeanXml = {
		render "MessageSource class = ${messageSource.class.name}"
	}
	
	def testComponent
	def testNamespaceConfig = {
		render "Component class = ${testComponent.class.name}"		
	}
	
	def anotherMessageSource
	def testSpringBeanAlias = {
		render "Aliased bean = ${anotherMessageSource.class.name}"
	}
	
	def testRequestScopedBean = {
		def one = applicationContext.getBean("requestScopedBean")
		
		one.name = "test"
		
		def two = request.getAttribute("requestScopedBean")
		
		render "Scoped bean = ${one == two}"
	}

}