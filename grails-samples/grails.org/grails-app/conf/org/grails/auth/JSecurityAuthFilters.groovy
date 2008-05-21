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
            def targetUri = d.request.forwardURI 
            if (d.request.queryString) {
                targetUri = "${targetUri}?${d.request.queryString}"
            }

            d.redirect(
                    controller: 'user',
                    action: 'login',
                    params:[originalURI: targetUri])
        }
    }	

    static filters = {
	   // Ensure that all controllers and actions require an authenticated user,

	        // Creating, modifying, or deleting a book requires the "Administrator"
	        // role.
	        wikiEditing(controller: "(content|news)", action: "(createNews|markupWikiPage|editWikiPage|createWikiPage|saveWikiPage)") {
	            before = {
	                accessControl {
	                    role("Editor") || role("Administrator")
	                }
	            }
	        }
            jobPosting(controller:"(job|paypal)", action:"(delete|edit|update|editJobs|save|create|buy|success|cancel)") {
	            before = {
	                accessControl {
	                    role("Editor") || role("Administrator")
	                }
	            }                
            }
            wikiManagement(controller:"content", action:"rollbackWikiVersion") {
                before = {
                    accessControl {
                        role("Administrator")
                    }
                }
            }

            adminArea(uri:"/admin/**") {
                before = {
                    accessControl {
                        role("Administrator")
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