class TestFlowController {

    def myFlow = { 
		first {
			on("go").to "next"
		}
		
		next {
			action {
				def next = getNextStep()
				"$next"()
			}
			on("one").to "end"
		}
		
		end()
	}
	
	def getNextStep() { "one" }
}
