class TestController {

    def index = { }

	def testCommand = { TestCommand cmd ->
		render "name: ${cmd.name.value}, age:${cmd.age}"
	}
}

class TestCommand {
	Integer age
	String name
	Nested nested = new Nested()
}

class Nested {
    String value
}