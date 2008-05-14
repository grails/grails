package org.grails.auth

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ArtefactHandler
import org.codehaus.groovy.grails.commons.ControllerArtefactHandler
import org.jsecurity.SecurityUtils

/**
* @author Graeme Rocher
* @since 1.0
*
* Created: Feb 26, 2008
*/
class JSecurityAuthFilters {
	
    /**
     * Called when an unauthenticated user trys to access a secured
     * page.
     */
    def onNotAuthenticated(subject, d) {
        if (d.request.xhr) {
            d.render(template:"/user/loginForm", model:[originalURI:d.request.forwardURI,
                                                        formData:d.params,
                                                        async:true,
                                                        message:"auth.not.logged.in"])
        }
        else {
            // Redirect to login page.
            def targetUri = d.request.forwardURI - d.request.contextPath
            if (d.request.queryString) {
                targetUri = "${targetUri}?${d.request.queryString}"
            }

            d.redirect(
                    controller: 'user',
                    action: 'login')
        }
    }	

    static filters = {
	   // Ensure that all controllers and actions require an authenticated user,
	        // except for the "public" controller
	        auth(controller: "*", action: "*") {
	            before = {
	                // Exclude the "public" controller.
	                if (controllerName == "user") return true
                    else if (controllerName == "news") return true
                    else if(controllerName == "content" && !actionName) return true
	                // This just means that the user must be authenticated. He does
	                // not need any particular role or permission.
	                accessControl { true } 
	            }
	        }

	        // Creating, modifying, or deleting a book requires the "Administrator"
	        // role.
	        wikiEditing(controller: "content", action: "(createNews|markupWikiPage|editWikiPage|createWikiPage|saveWikiPage|rollbackWikiVersion)") {
	            before = {
	                accessControl {
	                    role("Editor") || role("Administrator")
	                }
	            }
	        }

	        // Showing a book requires the "Administrator" *or* the "User" roles.
	        wikiShow(controller: "content", action: "(index|showNews|showWikiVersion|infoWikiPage|diffWikiVersion)") {
	            before = {
	                accessControl {
	                    role("Editor") || role("User") || role("Administrator")
	                }
	            }
	        }	
	
			userInRequest(controller:"*", action:"*") {
				before = {
					def subject = SecurityUtils.getSubject() 
					if(subject && subject?.principal) {
						request.user = User.findByLogin(subject.principal)						
					}
				}
			}
	}

}