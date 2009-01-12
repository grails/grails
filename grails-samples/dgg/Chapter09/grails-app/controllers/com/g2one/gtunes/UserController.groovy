package com.g2one.gtunes

class UserController {
	def login = { LoginCommand cmd ->
		if(request.method == 'POST') {
			if(!cmd.hasErrors()) {
				session.user = cmd.getUser()
				render(template:"welcomeMessage")
			}
			else {
				render(template:'loginForm', model:[loginCmd:cmd])
			}
		}
		else {
			render(view:'/store/index')
		}
	}
	
	def register = {
		if(request.method == 'POST') {
			def u = new User(params)
			if(User.findByLogin(params.login)) {
				return [user:u, message:"user.already.exists"]
			}
			else if(u.password != params.confirm) {
				u.errors.rejectValue("password", "user.password.dontmatch")
				return [user:u]
			}						
			else if(u.save()) {
				session.user = u
				redirect(controller:"store")
			}
			else {
				return [user:u]
			}
		}
	}
	
	def logout = {
		session.invalidate()
		redirect(controller:"store")
	}
}
class LoginCommand {
	String login
	String password
	private u
	User getUser() { 
		if(!u && login) 
			u = User.findByLogin(login, [fetch:[purchasedSongs:'join']])
		return u
	}
	static constraints = {
		login blank:false, validator:{ val, obj ->
			if(!obj.user)
				return "user.not.found"
		}
		password blank:false, validator:{ val, obj ->			
			if(obj.user && obj.user.password != val)
				return "user.password.invalid"
		}
	}
}
