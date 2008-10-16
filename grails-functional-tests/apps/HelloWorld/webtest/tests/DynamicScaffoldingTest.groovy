class DynamicScaffoldingTest extends grails.util.WebTest {

    void suite() {
        testDynamicCRUD()
    }

    def testDynamicCRUD() {
        webtest("Dynamic CRUD") {
            invoke url:"car/list"

            clickLink "New Car"
            setSelectField(name: "dateOfManufacture_day", text: "1")
            setSelectField(name: "dateOfManufacture_month", text: "January")
            setSelectField(name: "dateOfManufacture_year", text: "2001")
            setSelectField(name: "dateOfManufacture_year", text: "2020")
            setSelectField(name: "dateOfManufacture_hour", text: "12")
            setSelectField(name: "dateOfManufacture_minute", text: "00")
            setInputField(name: "engineCapacity", value: "12345")
            setInputField(name: "make", value: "Ferrari")
            clickButton "Create"

            verifyText("2020-01-01 12:00:00.0")
            verifyText("12345")
            verifyText("Ferrari")
            verifyTitle "Show Car"
            clickButton "Edit"

            setInputField(name: "engineCapacity", value: "54321")
            clickButton "Update"

            verifyTitle "Show Car"
            verifyText("54321")

            invoke url:"car/list"
            verifyText("Ferrari")

            previousResponse()

            clickButton "Delete"

            verifyTitle "Car List"
            not {
                verifyText("Ferrari")
            }
        }
    }

}
