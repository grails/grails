import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition

class AaaaService {

    PlatformTransactionManager transactionManager

    static transactional = true

    private static final TRANSACTION_DEF = new org.springframework.transaction.support.DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_MANDATORY)

    def serviceMethod() {
	  def result = transactionManager.getTransaction(TRANSACTION_DEF)
        println result
    }
}
