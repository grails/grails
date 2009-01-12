package com.g2one.gtunes

class UserTests extends grails.test.GrailsUnitTestCase {
	void testInvalidLogin() {
		mockDomain(User)
		def user = new User()
		
		assertFalse "Should have failed validation for null login",user.validate()
		assertEquals "nullable", user.errors.login
		
		user.login = "sh"
		assertFalse "Should have failed validation for short login",user.validate()
		assertEquals "size", user.errors.login

		user.login = "thisisgoingtobewaywaywaywaytoolong"

		assertFalse "Should have failed validation for long login",user.validate()
		assertEquals "size", user.errors.login
		
	}
	
}