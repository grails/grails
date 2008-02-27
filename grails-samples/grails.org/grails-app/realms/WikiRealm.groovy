import org.jsecurity.authc.AccountException
import org.jsecurity.authc.IncorrectCredentialException
import org.jsecurity.authc.UnknownAccountException
import org.grails.auth.User

class WikiRealm {
    static authTokenClass = org.jsecurity.authc.UsernamePasswordToken

    def credentialMatcher

    def authenticate(authToken) {
        log.info "Attempting to authenticate ${authToken.username} in DB realm..."
        def username = authToken.username

        // Null username is invalid
        if (username == null) {
            throw new AccountException('Null usernames are not allowed by this realm.')
        }

        // Get the user with the given username. If the user is not
        // found, then they don't have an account and we throw an
        // exception.
        def user = User.findByLogin(username)
        log.info "Found user '${user.login}' in DB"
        if (!user) {
            throw new UnknownAccountException("No account found for user [${username}]")
        }

        // Now check the user's password against the hashed value stored
        // in the database.
        if (!credentialMatcher.doCredentialsMatch(authToken.password, user.password)) {
            log.info 'Invalid password (DB realm)'
            throw new IncorrectCredentialException("Invalid password for user '${username}'")
        }

        return username
    }

    def hasRole(principal, roleName) {
        def user = User.findByLogin(principal.name)

        return null != user?.roles.find { it.name == roleName }
    }

    def hasAllRoles(principal, roles) {
        def criteria = User.createCriteria()
        def r = criteria.list {
            roles {
                'in'('name', roles)
            }
            eq('login', principal.name)
        }

        return r.size() == roles.size()
    }

    def isPermitted(principal, requiredPermission) {
        // no permission level authentication implemented yet
        return true
    }

 
}
