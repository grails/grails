import org.springframework.mock.jndi.*
import org.apache.commons.dbcp.*

includeTargets << grailsScript( "Bootstrap" )  

target(main:"the default target") {
	depends(parseArguments)
	
	def username = argsMap.user
	if(username==null) {
		println "--user argument not specified"
		exit(1)
	}
	def password = argsMap.pass
	if(password==null) {
		println "--pass argument not specified"
		exit(1)
	}
	
	def url = argsMap.url
	if(!url) {
		println "--url argument not specified"
		exit(1)
	}
	
	
	// mock out the production JNDI settings
	 def builder = new SimpleNamingContextBuilder();
	 def ds = new BasicDataSource()
	 ds.username = username
	 ds.password = password
	 ds.url = url
	 ds.driverClassName = "com.mysql.jdbc.Driver"
	
	 builder.bind("java:comp/env/jdbc/grailsSiteDS", ds);
	 builder.activate();	
	
	try {
		bootstrap()
		def pluginService = appCtx.getBean("pluginService")
		println "Syncing plugin list. Please wait.."
		pluginService.runMasterUpdate()
		println "Plugin list sync complete."
	}
	catch(e) {
		println "Error occurred syncing plugin list ${e.message}"
		e.printStackTrace()
	}

	
}
setDefaultTarget(main)