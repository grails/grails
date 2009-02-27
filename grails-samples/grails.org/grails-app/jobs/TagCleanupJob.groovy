class TagCleanupJob {
    def tagService

    def startDelay = 120000  // 120 second start-up
    def timeout = 86400000   // execute job every day

    def execute() {
        log.info "Starting tag cleanup..."
        tagService.removeEmptyTags()
    }

}
