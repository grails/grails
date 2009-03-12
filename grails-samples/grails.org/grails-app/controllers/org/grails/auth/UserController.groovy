package org.grails.auth

import org.grails.auth.User
import org.jsecurity.authc.UsernamePasswordToken
import org.jsecurity.authc.AuthenticationException
import org.apache.commons.codec.digest.DigestUtils
import org.grails.meta.UserInfo


/**
* @author Graeme Rocher
* @since 1.0
*
* Created: Feb 19, 2008
*/
class UserController {

    def scaffold = User

    def jsecSecurityManager

    def profile = {
        def userInfo = UserInfo.findByUser(request.user)
        if(request.method == 'POST') {
            if(!userInfo) userInfo = new UserInfo(user:request.user)
            userInfo.properties = params
            userInfo.save()

        }
        return [user:request.user, userInfo:userInfo]

    }

    def register = {
        def renderParams = [ model:[originalURI:params.originalURI,
                      formData:params,
                      async:request.xhr] ]
        
        if(request.xhr)
            renderParams.template = "registerForm"
        else
            renderParams.view = "register"

        if(request.method == 'POST') {
            def user = User.findByLogin(params.login)
            if(user) {

                renderParams.model.message= "auth.user.already.exists"
                render(renderParams)
            }
            else {
                if(params.password != params.password2) {
                    renderParams.model.message= "auth.password.mismatch"
                    render(renderParams)

                }
                else {

                    user = new User(login:params.login, password: (params.password ? DigestUtils.shaHex(params.password) : null), email:params.email)
                            .addToRoles(Role.findByName(Role.EDITOR))
                            .addToRoles(Role.findByName(Role.OBSERVER))

                    if(!user.hasErrors() && user.save(flush:true)) {
                        def userInfo = new UserInfo(params)
                        userInfo.user = user
                        userInfo.save()
                        
                        def authToken = new UsernamePasswordToken(user.login, params.password)
                        this.jsecSecurityManager.login(authToken)

                        if(params.originalURI) {

                            redirect(url:params.originalURI, params:params)
                        }
                        else {
                            redirect(uri:"/")
                        }
                    }
                    else {
                        renderParams.model.user = user
                        render(renderParams)
                    }
                }


            }
        }
        else {
            render(renderParams)
        }

    }

    def logout = {
        org.jsecurity.SecurityUtils.getSubject().logout()
        redirect(uri:"/")
    }

    def login = {
        if(request.method == 'POST') {
            def authToken = new UsernamePasswordToken(params.login, params.password)
            try {
                this.jsecSecurityManager.login(authToken)
                if(params.originalURI) {
                    // get rid of the login stuff before passing along params
                    params.remove('login')
                    params.remove('password')
                    params.remove('Submit')
                    def uri = params.remove('originalURI')
                    redirect(url:"${uri}${params.toQueryString()}")
                } else {
                    redirect(uri:"/")
                }
            } catch (AuthenticationException ex){
                if(request.xhr) {
                    render(template:"loginForm", model:[originalURI:params.originalURI,
                                                        formData:params,
                                                        async:true,
                                                        message:"auth.invalid.login"])
                } else {
                    flash.message = "Invalid username and/or password"
                    redirect(action: 'login', params: [ username: params.username, originalURI:params.originalURI ])
                }
            }
        } else {            
            render(view:"login")
        }
    }

}