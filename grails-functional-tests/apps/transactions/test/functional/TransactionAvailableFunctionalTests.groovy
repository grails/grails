class TransactionAvailableFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testTransactionAvailableGroovy() {
        get('/blah/index')
        assertStatus 200
        assertContentContains 'success'
    }

    void testTransactionAvailableJava() {
        get('/blah/java')
        assertStatus 200
        assertContentContains 'success'
    }

}
