package org.grails.auth

import org.jsecurity.context.support.ThreadLocalSecurityContext
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ArtefactHandler
import org.codehaus.groovy.grails.commons.ControllerArtefactHandler

/**
* @author Graeme Rocher
* @since 1.0
*
* Created: Feb 26, 2008
*/
class JSecurityAuthFilters {

    static filters = {
        loginFilter(controller:"*", action:"*") {
            before = {
                def securityContext = new ThreadLocalSecurityContext()
                def application = ApplicationHolder.getApplication()
                def controllerClass = application.getArtefactByLogicalPropertyName(ControllerArtefactHandler.TYPE, controllerName)
                def roleMap = controllerClass?.reference?.wrappedInstance.roleMap

                // Is this action configured for access control?
                if (!roleMap?.containsKey(actionName)
                        && !roleMap?.containsKey('*')) {
                    return true
                }

                if(!securityContext.isAuthenticated()) {
                    // User is not authenticated, so redirect to the
                    // login page.
                    if(request.xhr) {
                        render(template:"/user/loginForm", model:[originalURI:request.forwardURI,
                                                                  formData:params,
                                                                  async:true,
                                                                  message:"auth.not.logged.in"])
                    }
                    else {
                        redirect(controller: 'user', action: 'login')
                    }
                    return false
                }

                try {
                    request.user = User.findByLogin(securityContext.getPrincipal().getName())
                } catch (Exception e) {
                    def threadContext = ThreadLocalSecurityContext.current()
                    threadContext.invalidate()
                    flash.message = "auth.user.not.found.for.credentials"
                    redirect(controller: 'user', action: 'login')
                    return false
                }

                def requiredRoles = roleMap[actionName]
                if (requiredRoles == null) requiredRoles = []
                if (roleMap['*']) {
                    // Add any roles that apply to all actions.
                    requiredRoles.addAll(roleMap['*'])
                }

                if (!requiredRoles.isEmpty() && !securityContext.hasAllRoles(requiredRoles)) {
                    // User does not have the required roles. Redirect
                    // to an error page?
                    response.sendError(403)
                    return false
                }


                return true                
            }
        }
    }

}