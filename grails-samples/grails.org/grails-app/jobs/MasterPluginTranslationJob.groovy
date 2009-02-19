
class MasterPluginTranslationJob {
    def pluginService

    def startDelay = 120000 // 120 second start-up
    def timeout = 3600000   // execute job every hour
    
    def execute() {
        log.info "Starting master plugin translation..."
        pluginService.runMasterUpdate()
    }
    
}
