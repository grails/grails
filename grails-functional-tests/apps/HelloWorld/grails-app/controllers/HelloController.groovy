class HelloController extends BaseController {

    def index = {
        render("${g.message(code: 'default.paginate.prev')}: Hello world!")
    }

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

    /**
     * Test for GRAILS-3929. Makes sure that the stuff in "grails-app/conf"
     * is on the classpath.
     */
    def resourceTest = {
        String content = getClass().classLoader.rootLoader.getResourceAsStream("test-content.txt").text
        render content
    }
}
