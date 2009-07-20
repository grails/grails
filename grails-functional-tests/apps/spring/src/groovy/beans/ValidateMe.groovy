package beans

import org.codehaus.groovy.grails.validation.*

@Validateable
class ValidateMe {

	String name
	
	static constraints = {
		name size:1..12
	}

}