class TestTagLib {

	// test override message tag
	def message = { attrs, body ->
		out << "overriden"
	}
}
