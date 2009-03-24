class FormsControllerTest extends grails.util.WebTest {
    // Unlike unit tests, functional tests are often sequence dependent.
    // Specify that sequence here.
    void suite() {
        testDefaultAction()
        testSubmitButton()
        testActionSubmit()
    }

    def testDefaultAction() {
        webtest("Default action") {
            invoke     (url:  "forms/")
            verifyText (text: "failed")
        }
    }

    def testSubmitButton() {
        webtest("Tag <g:submitButton>") {
            // Show the form page containing the submit button.
            invoke         (url:   "forms/test2")
            verifyTitle    (text:  "Submit Button")
            verifyXPath    (xpath: "//form/@action", text: "/HelloWorld/forms/success")
            verifyCheckbox (name:  "doCheck", checked: false)

            // Fill in the fields on the form.
            setInputField (name: "name", value: "Eddy")
            setInputField (name: "memo", value: "This is a memo")

            // Now click on the button and check the result.
            clickButton    (name: "submit")
            verifyText     (text: "success")
            verifyText     (text: "Text field: Eddy")
            verifyText     (text: "Text area: This is a memo")
        }
    }

    def testActionSubmit() {
        webtest("Tag <g:actionSubmit>") {
            // Show the form page containing the submit button.
            invoke      (url:   "forms/test")
            verifyTitle (text:  "Action Submit")
//            verifyXPath (xpath: "//form/@action", text: "/HelloWorld/forms")
            // TODO: Uncomment line above when it starts working

            // Now click on the buttons and check the results.
            clickButton (xpath:  "//form/input[@value='Do it right']")
            verifyText  (text:  "success")

            invoke      (url:   "forms/test")
            clickButton (xpath:  "//form/input[@value='Do it wrong']")
            verifyText  (text:  "failed")
        }
    }
}
