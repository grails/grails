class TestController {

    def index = { }

	def testCommand = { TestCommand cmd ->
		render "name: ${cmd.name.value}, age:${cmd.age}"
	}
	
	def testCommandWithNestedValues = { TestCommand cmd ->
		render "name: ${cmd.name.value}, age:${cmd.age}, nested.value: ${cmd.nested.value}"
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