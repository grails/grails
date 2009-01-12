package com.g2one.gtunes

import grails.test.*

class UserControllerTests extends grails.test.ControllerUnitTestCase {
	
	void testPasswordsMatch() {
		controller.metaClass.sendMail = { Closure c -> } // stub out send mail method
		
		mockRequest.method = 'POST'
		mockDomain(User)
	
		mockParams.login = "joebloggs"
		mockParams.password = "password"
		mockParams.confirm = "different"		
		mockParams.firstName = "Joe"
		mockParams.lastName = "Blogs"
		mockParams.email = "joe@bloggs.com"
		
		def model = controller.register()
		
		assert model
		def user = model.user
		assert user.hasErrors()
		println user.errors
		assertEquals "user.password.dontmatch", user.errors.password		
	}
	
	void testRegistrationFailed() {
		mockRequest.method = 'POST'
		
		mockDomain(User)
	
		mockParams.login = ""
		def model = controller.register()
		
		assertNull mockSession.user
		assert model
		def user = model.user
		assert user.hasErrors()
		assertEquals "blank", user.errors.login				
		assertEquals "nullable", user.errors.password						
		assertEquals "nullable", user.errors.firstName						
		assertEquals "nullable", user.errors.lastName								
	}
	
	void testRegistrationSuccess() {
		mockRequest.method = 'POST'
		mockDomain(User)

		mockParams.login = "joebloggs"
		mockParams.password = "password"
		mockParams.confirm = "password"		
		mockParams.firstName = "Joe"
		mockParams.lastName = "Blogs"
		mockParams.email = "joe@bloggs.com"		

		def model = controller.register()
		assertEquals 'store',redirectArgs.controller
		assertNotNull mockSession.user
	}

	void testLoginUserNotFound() {
		mockRequest.method = 'POST'
		mockDomain(User)
		MockUtils.prepareForConstraintsTests(LoginCommand)				
		def cmd = new LoginCommand(login:"fred", password:"letmein")
		cmd.validate()
		def errors = cmd.errors
		controller.login(cmd)
		
		assertTrue cmd.hasErrors()
		assertEquals "user.not.found", errors.login
		assertEquals "loginForm", renderArgs.template
	}
	
	void testLoginPasswordInvalid() {
		mockRequest.method = 'POST'
		mockDomain(User, [new User(login:"fred", password:"realpassword")])
		MockUtils.prepareForConstraintsTests(LoginCommand)				
		def cmd = new LoginCommand(login:"fred", password:"letmein")
		cmd.validate()
		controller.login(cmd)
		def errors = cmd.errors
		assertTrue cmd.hasErrors()
		assertEquals "user.password.invalid", errors.password
		
		assertEquals "loginForm", controller.renderArgs.template		
	}
	
	void testLoginSuccess() {
		mockRequest.method = 'POST'
		mockDomain(User, [new User(login:"fred", password:"letmein")])
		MockUtils.prepareForConstraintsTests(LoginCommand)				
		def cmd = new LoginCommand(login:"fred", password:"letmein")
		cmd.validate()
		controller.login(cmd)
		def errors = cmd.errors
		assertFalse cmd.hasErrors()
		assertNotNull mockSession.user
		assertEquals "welcomeMessage", renderArgs.template
	}
}