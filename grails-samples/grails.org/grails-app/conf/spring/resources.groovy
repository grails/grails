import org.springframework.cache.ehcache.EhCacheFactoryBean
import org.grails.content.notifications.ContentAlertStack
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.wiki.GrailsWikiEngineFactoryBean
import org.radeox.engine.context.BaseInitialRenderContext

// Place your Spring DSL code here
beans = {
    textCache(EhCacheFactoryBean) {
        timeToLive = 300
    }
    downloadCache(EhCacheFactoryBean) {
        timeToLive = 300
    }
    contentCache(EhCacheFactoryBean) {
        timeToLive = 300
    }
    wikiCache(EhCacheFactoryBean) {
        timeToLive = 300
    }
	pluginListCache(EhCacheFactoryBean) {
		timeToLive = 600
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