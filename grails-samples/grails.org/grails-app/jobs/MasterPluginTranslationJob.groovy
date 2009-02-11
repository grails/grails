
class MasterPluginTranslationJob {
    def pluginService

    def startDelay = 30000l
    def timeout = 30000  // execute job every hour
    
    def execute() {
        println "TRANSLATING!!!"
        log.info "Starting master plugin translation..."
        pluginService.runMasterUpdate()
    }
    
}
