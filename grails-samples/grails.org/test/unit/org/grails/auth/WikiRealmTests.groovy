package org.grails.auth

import org.jsecurity.authc.*
import org.apache.commons.logging.LogFactory

/**
* @author Graeme Rocher
* @since 1.0
*
* Created: Feb 28, 2008
*/
class WikiRealmTests extends GroovyTestCase {

    void testAuthenticate() {
        WikiRealm.metaClass.getLog = {-> LogFactory.getLog(WikiRealm) }
        def realm = new WikiRealm()

        shouldFail(AccountException) {
            realm.authenticate( [:] )
        }

        User.metaClass.static.findByLogin = { String s -> }

        shouldFail(UnknownAccountException) {
            realm.authenticate username:"Fred"
        }

        realm.credentialMatcher = [doCredentialsMatch:{ String username, String password -> false }]

        User.metaClass.static.findByLogin = { String s -> new User(login:"Fred") }

        shouldFail(IncorrectCredentialException) {
            realm.authenticate username:"Fred"
        }

        realm.credentialMatcher = [doCredentialsMatch:{ String username, String password -> true }]

        assertEquals "Fred", realm.authenticate(username:"Fred")
    }

    void testHasRole() {
        User.metaClass.static.findByLogin = { String s -> null }

        WikiRealm.metaClass.getLog = {-> LogFactory.getLog(WikiRealm) }
        def realm = new WikiRealm()

        assertFalse realm.hasRole([name:"Fred"], "Administrator")

        User.metaClass.getRoles={-> [] }
        User.metaClass.static.findByLogin = { String s -> new User(login:"Fred") }

        assertFalse realm.hasRole([name:"Fred"], "Administrator")

        User.metaClass.getRoles={-> [[name:"Administrator"]] }

        assertTrue realm.hasRole([name:"Fred"], "Administrator")

    }

    void testHasAllRoles() {
        WikiRealm.metaClass.getLog = {-> LogFactory.getLog(WikiRealm) }
        def realm = new WikiRealm()

        User.metaClass.static.createCriteria = {-> [list:{Closure c -> [] }] }

        assertFalse realm.hasAllRoles( [name:"Fred"], ["Administrator", "Editor"])

        User.metaClass.static.createCriteria = {-> [list:{Closure c -> ["Editor"] }] }

        assertFalse realm.hasAllRoles( [name:"Fred"], ["Administrator", "Editor"])

        User.metaClass.static.createCriteria = {-> [list:{Closure c -> ["Editor", "Administrator"] }] }

        assertTrue realm.hasAllRoles( [name:"Fred"], ["Administrator", "Editor"])
    }


    void testIsPermitted() {
        // permissions not implemented, placeholder
        def realm = new WikiRealm()
        assertTrue realm.isPermitted(null,null)
    }
}