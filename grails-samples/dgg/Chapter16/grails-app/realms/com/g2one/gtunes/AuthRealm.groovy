package com.g2one.gtunes

import org.jsecurity.authc.AccountException
import org.jsecurity.authc.IncorrectCredentialsException
import org.jsecurity.authc.UnknownAccountException
import org.jsecurity.authc.SimpleAccount

import com.g2one.gtunes.User
import org.jsecurity.authc.credential.CredentialsMatcher

class AuthRealm {
  static authTokenClass = org.jsecurity.authc.UsernamePasswordToken

    CredentialsMatcher credentialMatcher

    def authenticate(authToken) {
        log.debug "Attempting to authenticate ${authToken.username} in DB realm..."
        def username = authToken.username

        // Null username is invalid
        if (username == null) {
            throw new AccountException('Null usernames are not allowed by this realm.')
        }

        // Get the user with the given username. If the user is not
        // found, then they don't have an account and we throw an
        // exception.
        def user = User.findByLogin(username)
        log.debug "Found user '${user?.login}' in DB"
        if (!user) {
            throw new UnknownAccountException("No account found for user [${username}]")
        }

        // Now check the user's password against the hashed value stored
        // in the database.
        def account = new SimpleAccount(username, user.password, "gTunesRealm")
        if (!credentialMatcher.doCredentialsMatch(authToken, account)) {
            log.debug 'Invalid password (DB realm)'
            throw new IncorrectCredentialsException("Invalid password for user '${username}'")
        }

        return username
    }

    def hasRole(principal, roleName) {
        def user = User.findByLogin(principal, [fetch:[roles:'join']])
        return user.roles.any { it.name == RoleName.valueOf(roleName) }
    }

    def hasAllRoles(principal, userRoles) {
        def foundRoles = User.withCriteria {
            roles {
                'in'('name', roles.collect { RoleName.valueOf(it) })
            }
            eq('login', principal)
        }

        return foundRoles.size() == roles.size()
    }

    def isPermitted(principal, requiredPermission) {
		if(requiredPermission instanceof Permission) {
			def permissions = Permission.withCriteria {
				user {
					eq('login', principal)
				}
			}
			return permissions.any { permission -> permission.implies(requiredPermission) }			
		}
		else {
			return true
		}
    }	
}