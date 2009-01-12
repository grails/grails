import org.jsecurity.crypto.hash.Sha1Hash
import com.g2one.gtunes.*

class BootStrap {

     def init = { servletContext ->
		def adminUser = User.findByLogin("admin")
		if(!adminUser) {
			new User(login:"administrator", 
					 password:new Sha1Hash("letmein").toHex(),
					 firstName:"Admin",
					 lastName:"User",
					 email:"admin@grails.org")
					.addToRoles(name:RoleName.ADMINISTRATOR)
					.save(flush:true)
		}	
     }
     def destroy = {
     }
} 