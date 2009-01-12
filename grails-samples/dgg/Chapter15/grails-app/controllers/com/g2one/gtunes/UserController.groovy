package com.g2one.gtunes

import org.jsecurity.authc.UsernamePasswordToken
import org.jsecurity.authc.AuthenticationException
import org.jsecurity.crypto.hash.Sha1Hash
import org.jsecurity.SecurityUtils

class UserController {
	
    def jsecSecurityManager

	def music = {
		def albumList = AlbumPayment.withCriteria {
			projections {
				property "album"
			}
			eq("user", request.user)
		}
		
		def artistList = albumList.collect { it.artist }.unique()		
		return [artists:artistList, albums:albumList ]
	}
	
	def login = { LoginCommand cmd ->
		if(request.method == 'POST') {
			if(!cmd.hasErrors()) {
				request.user = User.findByLogin(cmd.login)
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
			else {
				if(u.validate()) {
					u.password = new Sha1Hash(u.password).toHex()
					u.addToRoles(name:RoleName.USER)
					u.save()
					
			         def authToken = new UsernamePasswordToken(u.login, params.password)
             		this.jsecSecurityManager.login(authToken)			
					if(params.newsletter) {
						new Subscription(type:SubscriptionType.NEWSLETTER, user:u).save()
					}
			
					try {
						sendMail {
							to u.email
							subject "Registration Confirmation"
							body view:"/emails/confirmRegistration",
								 model:[user:u]
						}					
					}
					catch(Exception e) {
						log.error "Problem sending email $e.message", e
					}
					redirect(controller:"store", action:"shop")
				}
				else {
					return [user:u]
				}					
			}
		}
	}
	
	def logout = {
        org.jsecurity.SecurityUtils.getSubject().logout()
		redirect(controller:"store")
	}
}
class LoginCommand {
	String login
	String password
	
    def jsecSecurityManager
	
	boolean authenticate() {
         def authToken = new UsernamePasswordToken(login, password)
         try{
             this.jsecSecurityManager.login(authToken)
			 return true
		 }
         catch (AuthenticationException ex){
			return false
		 }		
	}
	static constraints = {
		login blank:false, validator:{ val, cmd ->
			if(!cmd.authenticate())
				return "user.invalid.login"
		}
		password blank:false
	}
}
