class FormsController {
    def defaultAction = "failed"

    def failed = {
        render("failed")
    }

    def success = {
        render("success - Text field: ${params['name']}, Text area: ${params['memo']}, Checkbox: ${params['doCheck']}")
    }

    def test = {}

    def test2 = {}
}
