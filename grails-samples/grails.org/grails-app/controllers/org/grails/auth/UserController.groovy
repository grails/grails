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

    def jsecSecurityManager

    def register = {
        if(request.method == 'POST') {
            def user = User.findByLogin(params.login)
            if(user) {
                render(template:"registerForm", model:[originalURI:params.originalURI,
                        formData:params,
                        async:true,
                        message:"auth.user.already.exists"])
            }
            else {
                if(params.password != params.password2) {
                    render(template:"registerForm", model:[originalURI:params.originalURI,
                            formData:params,
                            async:true,
                            message:"auth.password.mismatch"])

                }
                else {

                    user = new User(login:params.login, password: DigestUtils.shaHex(params.password), email:params.email)
                            .addToRoles(name:"Editor")
                            .addToRoles(name:"Observer")

                    if(!user.hasErrors() && user.save()) {
                        def userInfo = new UserInfo(user:user)
                        userInfo.properties = params['info']
                        userInfo.save()
                        
                        def authToken = new UsernamePasswordToken(user.login, params.password)
                        this.jsecSecurityManager.login(authToken)

                        if(params.originalURI) {
                            redirect(url:params.originalURI, params:params)
                        }
                        else {
                            redirect(uri:"")
                        }
                    }
                    else {
                        render(template:"registerForm", model:[originalURI:params.originalURI,
                                formData:params,
                                async:true,
                                user:user])
                    }
                }


            }
        }
        else {
            render(template:"registerForm", model:[originalURI:params.originalURI,
                    formData:params,
                    async:true,
                    message:"auth.invalid.login"])
        }

    }

    def logout = {
        redirect(uri:"")
    }

    def login = {
        if(request.method == 'POST') {
            def authToken = new UsernamePasswordToken(params.login, params.password)
            try{
                this.jsecSecurityManager.login(authToken)
                if(params.originalURI) {
                    redirect(url:params.originalURI, params:params)
                }
                else {
                    redirect(uri:"")
                }
            }
            catch (AuthenticationException ex){
                if(request.xhr) {
                    render(template:"loginForm", model:[originalURI:params.originalURI,
                                                        formData:params,
                                                        async:true,
                                                        message:"auth.invalid.login"])
                }
                else {
                    flash.message = "Invalid username and/or password"
                    redirect(action: 'login', params: [ username: params.username ])
                }
            }

        }
        else {            
            render(view:"login")
        }
    }

}