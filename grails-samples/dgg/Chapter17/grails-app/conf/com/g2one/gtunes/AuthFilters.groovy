package com.g2one.gtunes

import org.jsecurity.SecurityUtils

class AuthFilters {
	
    def onNotAuthenticated(subject, d) {
        if (d.request.xhr) {
            d.render(template:"/user/loginForm", model:[message:"user.not.logged.in"])
        }
        else {
            // Redirect to login page.
			d.flash.message = "user.not.logged.in"
			if(d.actionName == 'buy') {
				d.redirect(controller:"album", action:"display", id:d.params.id)
			}
			else {
	            d.redirect(controller:"store", action:"shop")
			}
        }
    }
	
	static filters = {
		blogEditing(controller:"blog", action:"(create|save)") {
			before = {
				accessControl {
					
					role(RoleName.ADMINISTRATOR.name())
				}
			}
		}
		admin(uri:'/admin/**') {
			before = {
				accessControl {
					role(RoleName.ADMINISTRATOR.name())
				}
			}			
		}
		purchasing(controller:"store", action:"buy") {
			before = {
				accessControl()
			}
		}
		library(controller:"user", action:"music") {
			before = {
				accessControl()
			}			
		}
		musicAccess(controller:"(artist|song)", action:"(play|subscribe|unsubscribe|stream)") {
			before = {
				accessControl()
			}						
		}
        userInRequest(controller:"*", action:"*") {
			before = {
				def subject = SecurityUtils.getSubject() 
				if(subject && subject?.principal) {
					request.user = User.findByLogin(subject.principal, [fetch:[purchasedSongs:'join']])						
				}
			}
		}		
	}
}