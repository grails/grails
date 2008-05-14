/* Copyright 2004-2005 Graeme Rocher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.auth;

import groovy.util.GroovyTestCase
import org.jsecurity.authc.AuthenticationException
import org.grails.meta.UserInfo


/**
 * @author Graeme Rocher
 * @since 1.0
 *        <p/>
 *        Created: Feb 28, 2008
 */
public class UserControllerTests extends GroovyTestCase {

    void testRegisterGET() {
        def renderParams = [:]
        UserController.metaClass.getRequest = {-> [method:"GET"] }
        UserController.metaClass.render = { Map args -> renderParams = args }
        def params = [originalURI:"/foo/bar", more:"stuff"]
        UserController.metaClass.getParams = {->  params}

        def controller = new UserController()
        controller.register()

        assertEquals "registerForm", renderParams.template
        assertEquals "auth.invalid.login", renderParams?.model?.message
        assertEquals true, renderParams?.model?.async
        assertEquals  "/foo/bar", renderParams.model?.originalURI
        assertEquals  params, renderParams.model?.formData

    }

    void testRegisterUserExists() {
        def renderParams = [:]
        def params = [originalURI:"/foo/bar", login:"Fred"]


        UserController.metaClass.getRequest = {-> [method:"POST"] }
        UserController.metaClass.render = { Map args -> renderParams = args }
        UserController.metaClass.getParams = {->  params}

        User.metaClass.static.findByLogin = {String login-> new User(login:login) }

        def controller = new UserController()
        controller.register()

        assertEquals "registerForm", renderParams.template
        assertEquals "auth.user.already.exists", renderParams?.model?.message
        assertEquals true, renderParams?.model?.async
        assertEquals  "/foo/bar", renderParams.model?.originalURI
        assertEquals  params, renderParams.model?.formData
         
    }

    void testRegisterWithNonMatchingPasswords() {
       def renderParams = [:]
        def params = [originalURI:"/foo/bar", login:"Fred", password:"one", password2:"two"]


        UserController.metaClass.getRequest = {-> [method:"POST"] }
        UserController.metaClass.render = { Map args -> renderParams = args }
        UserController.metaClass.getParams = {->  params}

        User.metaClass.static.findByLogin = {String login-> null }

        def controller = new UserController()
        controller.register()

        assertEquals "registerForm", renderParams.template
        assertEquals true, renderParams?.model?.async
        assertEquals  "/foo/bar", renderParams.model?.originalURI
        assertEquals  params, renderParams.model?.formData
    }

    void testRegisterWithFormErrors() {
      def renderParams = [:]
        def params = [originalURI:"/foo/bar", login:"Fred", password:"one", password2:"one"]


        UserController.metaClass.getRequest = {-> [method:"POST"] }
        UserController.metaClass.render = { Map args -> renderParams = args }
        UserController.metaClass.getParams = {->  params}

        User.metaClass.static.findByLogin = {String login-> null }
        User.metaClass.addToRoles = { Map m -> delegate }
        User.metaClass.hasErrors = {-> true }

        def controller = new UserController()
        controller.register()

        assertEquals "registerForm", renderParams.template
        assert  renderParams?.model?.user
        assertEquals true, renderParams?.model?.async
        assertEquals  "/foo/bar", renderParams.model?.originalURI
        assertEquals  params, renderParams.model?.formData
    }

    void testRegisterAndRedirectToOriginalPage() {
          def redirectParams = [:]
          def params = [originalURI:"/foo/bar", login:"Fred", password:"one", password2:"one"]


          UserController.metaClass.redirect = {Map m-> redirectParams = m }
          UserController.metaClass.getRequest = {-> [method:"POST"] }
          UserController.metaClass.getParams = {->  params}

          User.metaClass.static.findByLogin = {String login-> null }
          User.metaClass.addToRoles = { Map m -> delegate }
          User.metaClass.hasErrors = {-> false }
          User.metaClass.save = {-> delegate }
          UserInfo.metaClass.setProperties = {Map m->}
          UserInfo.metaClass.save = {}

            def controller = new UserController()
            def authenticateCalled = false
            controller.jsecSecurityManager = [login: { authenticateCalled = true }]
            controller.register()

        
          assert authenticateCalled

          assertEquals params.originalURI, redirectParams.url
          assertEquals params, redirectParams.params
    }

    void testRedirectWithoutOriginalPage() {
         def redirectParams = [:]
          def params = [ login:"Fred", password:"one", password2:"one"]


          UserController.metaClass.redirect = {Map m-> redirectParams = m }
          UserController.metaClass.getRequest = {-> [method:"POST"] }
          UserController.metaClass.getParams = {->  params}

          User.metaClass.static.findByLogin = {String login-> null }
          User.metaClass.addToRoles = { Map m -> delegate }
          User.metaClass.hasErrors = {-> false }
          User.metaClass.save = {-> delegate }
        UserInfo.metaClass.setProperties = {Map m->}
        UserInfo.metaClass.save = {}

            def controller = new UserController()
            def authenticateCalled = false
            controller.jsecSecurityManager = [login: { authenticateCalled = true }]
            controller.register()


          assert authenticateCalled

          assertEquals "", redirectParams.uri
    }

    /*void testLogout() {
         def redirectParams = [:]
         UserController.metaClass.redirect = {Map m-> redirectParams = m }

         def invalidateCalled = false
         org.jsecurity.context.support.ThreadLocalSecurityContext.metaClass.static.current ={-> [invalidate:{invalidateCalled=true}] }


         def controller = new UserController()

         controller.logout()

         assert invalidateCalled
         assertEquals "", redirectParams.uri
    }*/

    void testLoginWithGET() {
        def renderParams = [:]
        UserController.metaClass.getRequest = {-> [method:"GET"] }
        UserController.metaClass.render = { Map args -> renderParams = args }

        def controller = new UserController()

        controller.login()

        assertEquals "login",renderParams.view
    }

    void testLoginFailureWithAjaxRequest() {
        def renderParams = [:]
        UserController.metaClass.getParams = {-> [originalURI:"/foo/bar", username:"fred", password:"letmein"] }
        UserController.metaClass.getRequest = {-> [method:"POST", xhr:true] }
        UserController.metaClass.render = { Map args -> renderParams = args }



        def controller = new UserController()

        controller.jsecSecurityManager = [login:{ throw new AuthenticationException("incorrect password") }]

        controller.login()

        println renderParams
        assertEquals "loginForm", renderParams.template
        assertEquals "auth.invalid.login", renderParams?.model?.message
        assertEquals true, renderParams?.model?.async
        assertEquals  "/foo/bar", renderParams.model?.originalURI
        
    }

    void testLoginFailureWithRegularRequest() {
        def redirectParams = [:]
        UserController.metaClass.redirect = {Map m-> redirectParams = m }

        UserController.metaClass.getParams = {-> [originalURI:"/foo/bar", username:"fred", password:"letmein"] }
        UserController.metaClass.getRequest = {-> [method:"POST", xhr:false] }
        UserController.metaClass.getFlash = {-> [:]}


        def controller = new UserController()

        controller.jsecSecurityManager = [login:{ throw new AuthenticationException("incorrect password") }]

        controller.login()

        assertEquals 'login', redirectParams.action
        assertEquals 'fred', redirectParams.params?.username
    }

    void testLoginSuccessWithOriginalPage() {
        def redirectParams = [:]
        UserController.metaClass.redirect = {Map m-> redirectParams = m }

        def params = [originalURI:"/foo/bar", username:"fred", password:"letmein"]
        UserController.metaClass.getParams = {-> params }
        UserController.metaClass.getRequest = {-> [method:"POST", xhr:false] }
        UserController.metaClass.getFlash = {-> [:]}


        def controller = new UserController()

        controller.jsecSecurityManager = [login:{ true }]

        controller.login()


        assertEquals "/foo/bar", redirectParams.url
        assertEquals params, redirectParams.params
    }

    void testLoginSuccessWithoutOriginalPage() {
        def redirectParams = [:]
        UserController.metaClass.redirect = {Map m-> redirectParams = m }

        def params = [username:"fred", password:"letmein"]
        UserController.metaClass.getParams = {-> params }
        UserController.metaClass.getRequest = {-> [method:"POST", xhr:false] }
        UserController.metaClass.getFlash = {-> [:]}


        def controller = new UserController()

        controller.jsecSecurityManager = [login:{ true }]

        controller.login()


        assertEquals "", redirectParams.uri
    }
}
