class PerMethodTransactionFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testTransactionsEnabled() {
        get('/springTransaction/testOne')
        assertStatus 200
        assertContentContains 'hasTransaction = true'
    }

    void testTransactionsDisabled() {
        get('/springTransaction/testTwo')
        assertStatus 200
        assertContentContains 'hasTransaction = false'
    }

}
