grailsHome = ant.project.properties."environment.GRAILS_HOME"

includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )  

target(main: "The description of the script goes here!") {
	depends(parseArguments)
	def message = "Hello ${argsMap.params ? argsMap.params[0] : 'World'}"
	if(argsMap.uppercase) {
		echo message.toUpperCase()
	}
	else {
		echo message
	}
}

setDefaultTarget(main)