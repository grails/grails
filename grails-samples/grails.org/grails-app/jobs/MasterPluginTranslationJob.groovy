
class MasterPluginTranslationJob {
    def pluginService

    def startDelay = 30000 // 120 second start-up
    def timeout = 3600000   // execute job every hour
    
    def execute() {
        log.info "Starting master plugin translation..."
        pluginService.runMasterUpdate()
    }
    
}
