import org.grails.*
import grails.util.GrailsUtil
import org.grails.auth.User
import javax.servlet.http.HttpServletRequest
import org.apache.commons.codec.digest.DigestUtils
import org.grails.auth.Role

class BootStrap {

     def init = { servletContext ->

            HttpServletRequest.metaClass.isXhr = {->
                 'XMLHttpRequest' == delegate.getHeader('X-Requested-With')                
            }

            def admin = User.findByLogin("admin")
			if(!admin) {
                def password = System.getProperty("initial.admin.password")
                if(!password) {
                    throw new Exception("""
During the first run you must specify a password to use for the admin account. For example:

grails -Dinitial.admin.password=changeit run-app""")
                }
                else {
                    assert new User(login:"admin", email:"info@g2one.com",password:DigestUtils.shaHex(password))
                            .addToRoles(name:Role.ADMINSITRATOR)
                            .addToRoles(name:Role.EDITOR)
                            .addToRoles(name:Role.OBSERVER)
                            .save(flush:true)
                }
			}
            else {
                if(!admin.roles) {
                    admin
                         .addToRoles(name:Role.ADMINSITRATOR)
                         .addToRoles(name:Role.EDITOR)
                         .addToRoles(name:Role.OBSERVER)
                         .save(flush:true)
                }
            }


     }
     def destroy = {
     }
} 