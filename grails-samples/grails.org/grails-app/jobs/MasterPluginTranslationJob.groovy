
class MasterPluginTranslationJob {
    def pluginService

    def startDelay = 120000 // 120 second start-up
    def timeout = 7200000   // execute job 2 hours
    
    def execute() {
        log.info "Starting master plugin translation..."
        pluginService.runMasterUpdate()
    }
    
}
