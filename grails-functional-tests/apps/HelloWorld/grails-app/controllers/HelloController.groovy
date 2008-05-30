class HelloController {

    def index = { render("Hello world!") }

    def mapping = {
        render "Controller: ${params['controller']}, action: ${params['action']}, other: ${params['something']}"
    }

    // Sends an internal server error code back to the client.
    def bad = {
        response.sendError(500)
    }

    def error414 = {
        response.sendError(414)
    }

    def error444 = {
        response.sendError(444)
    }

    def reloadTest = {}
}
