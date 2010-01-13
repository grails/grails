import org.codehaus.groovy.grails.commons.*

class TestController {

    def read = {
		def one = grailsApplication.config.test.value1
		def two = ConfigurationHolder.config.test.value2
		def three = ConfigurationHolder.flatConfig['test.value3']
		
		render "one = $one, two = $two, three = $three"
	}
}
