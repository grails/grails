import org.springframework.transaction.annotation.*
import org.springframework.transaction.interceptor.TransactionAspectSupport


class SpringConfiguredService {

	@Transactional
	def methodOne() {
		def status
		try {
	        status = TransactionAspectSupport.currentTransactionStatus()
		}catch(e) {			
		}

        return "hasTransaction = ${status!=null}"		
	}
	
	// should not be transactional
	def methodTwo() {
		def status
		try {
	        status = TransactionAspectSupport.currentTransactionStatus()
		}catch(e) {			
		}

        return "hasTransaction = ${status!=null}"				
	}

}