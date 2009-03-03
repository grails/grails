// Place your Spring DSL code here
beans = {
    albumArtCache(org.springframework.cache.ehcache.EhCacheFactoryBean) {
		timeToLive = 300
	}
	
	
	jmsFactory(org.apache.activemq.ActiveMQConnectionFactory) { bean ->
		bean.destroyMethod = "stop"
		brokerURL = "tcp://localhost:61616"			
	}
	
	jmsTemplate(org.springframework.jms.core.JmsTemplate) {
		connectionFactory = jmsFactory
	}
	jmsMessageListener(org.springframework.jms.listener.adapter.MessageListenerAdapter, ref("storeService")) {
		defaultListenerMethod = "onNewAlbum"
	}
	jmsContainer(org.springframework.jms.listener.DefaultMessageListenerContainer) {
		connectionFactory = jmsFactory
		destinationName = "artistSubscriptions"
		messageListener = jmsMessageListener
		transactionManager = ref("transactionManager")
		autoStartup = false
	}
	
	streamingService(com.g2one.gtunes.StreamingService) 
}