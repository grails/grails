class SimpleCacheGrailsPlugin {
    def version = 0.1

    def author = "Graeme Rocher"
    def authorEmail = "graeme@g2one.com"
    def title = "Simple caching plugin"
    def description = 'A plugin that provides simple application and content layer caching'

	def doWithSpring = {
	    globalCache(org.springframework.cache.ehcache.EhCacheFactoryBean) {
			timeToLive = 300
		}		
	}
   
	def doWithDynamicMethods = { applicationContext ->
		def cacheService = applicationContext.getBean("cacheService")
		application.controllerClasses*.metaClass*.cacheOrReturn = { Serializable cacheKey, Closure callable ->
			cacheService.cacheOrReturn(cacheKey, callable)
		}
	}
	
}
