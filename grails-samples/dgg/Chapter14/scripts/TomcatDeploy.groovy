grailsHome = ant.project.properties."environment.GRAILS_HOME"
tomcatHome = ant.project.properties."environment.TOMCAT_HOME"

includeTargets << new File ( "${grailsHome}/scripts/War.groovy" )  

ant.path(id:"tomcat.lib.path") {
	fileset(dir:"${tomcatHome}/server/lib",includes:"*.jar")
}
ant.taskdef(name:"deploy",classname:"org.apache.catalina.ant.DeployTask", classpathref:"tomcat.lib.path")
target(main: "Deploys the Grails application to Tomcat") {
	depends(parseArguments, war)
	def dest = argsMap.params ? argsMap.params[0] : "http://localhost:8080/manager"
	
	deploy(war:warName,
		   url:dest,
		   path:serverContextPath,
		   username:"deployer",
		   password:"secret")
}
setDefaultTarget(main)
