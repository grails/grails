

class TestController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']


    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ testInstanceList: Test.list( params ), testInstanceTotal: Test.count() ]
    }

    def show = {
        def testInstance = Test.get( params.id )

        if(!testInstance) {
            flash.message = "Test not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ testInstance : testInstance ] }
    }

    def delete = {
        def testInstance = Test.get( params.id )
        if(testInstance) {
            try {
                testInstance.delete()
                flash.message = "Test ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Test ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Test not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def testInstance = Test.get( params.id )

        if(!testInstance) {
            flash.message = "Test not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ testInstance : testInstance ]
        }
    }

    def update = {
        def testInstance = Test.get( params.id )
        if(testInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(testInstance.version > version) {
                    
                    testInstance.errors.rejectValue("version", "test.optimistic.locking.failure", "Another user has updated this Test while you were editing.")
                    render(view:'edit',model:[testInstance:testInstance])
                    return
                }
            }
            testInstance.properties = params
            if(!testInstance.hasErrors() && testInstance.save()) {
                flash.message = "Test ${params.id} updated"
                redirect(action:show,id:testInstance.id)
            }
            else {
                render(view:'edit',model:[testInstance:testInstance])
            }
        }
        else {
            flash.message = "Test not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def testInstance = new Test()
        testInstance.properties = params
        return ['testInstance':testInstance]
    }

    def save = {
        def testInstance = new Test(params)
        if(!testInstance.hasErrors() && testInstance.save()) {
            flash.message = "Test ${testInstance.id} created"
            redirect(action:show,id:testInstance.id)
        }
        else {
            render(view:'create',model:[testInstance:testInstance])
        }
    }
}
