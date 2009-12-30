import org.springframework.cache.ehcache.EhCacheFactoryBean
import org.grails.content.notifications.ContentAlertStack
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.wiki.GrailsWikiEngineFactoryBean
import org.radeox.engine.context.BaseInitialRenderContext

// Place your Spring DSL code here
beans = {
    textCache(EhCacheFactoryBean) {
        timeToLive = 300
		maxElementsInMemory = 100
		overflowToDisk=false
    }
    downloadCache(EhCacheFactoryBean) {
        timeToLive = 300
		maxElementsInMemory = 100
		overflowToDisk=false		
    }
    contentCache(EhCacheFactoryBean) {
        timeToLive = 300
		maxElementsInMemory = 200
		overflowToDisk=false		
    }
    wikiCache(EhCacheFactoryBean) {
        timeToLive = 300
		maxElementsInMemory = 200
		overflowToDisk=false		
    }
	pluginListCache(EhCacheFactoryBean) {
		timeToLive = 600
		maxElementsInMemory = 100		
		overflowToDisk=false		
	}
    contentToMessage(ContentAlertStack)
    wikiContext(BaseInitialRenderContext)
    wikiEngine(GrailsWikiEngineFactoryBean) {
        cacheService = ref('cacheService')
        def config = ConfigurationHolder.getConfig()
        contextPath = config.grails.serverURL
        context = wikiContext
    }

}