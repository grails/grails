class TestController {

    def index = { }

	def testCommand = { TestCommand cmd ->
		render "name: ${cmd.name}, age:${cmd.age}"
	}
}
class TestCommand {
	String name
	Integer age
}
