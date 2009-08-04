includeTargets << grailsScript( "Bootstrap" )  

target(main:"the default target") {
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