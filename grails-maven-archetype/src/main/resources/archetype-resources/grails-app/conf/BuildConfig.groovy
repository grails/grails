grails.war.dependencies = {
    grailsSettings.runtimeDependencies?.each { File f ->
        fileset(dir: f.parent, includes: f.name)
    }
}
