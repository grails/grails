class ErrorsController {
    def show = {
        render "Internal server error: ${params['code']}"
    }
}