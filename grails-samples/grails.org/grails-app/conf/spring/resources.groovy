import org.springframework.cache.ehcache.EhCacheFactoryBean
import org.grails.content.notifications.ContentAlertStack
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.wiki.GrailsWikiEngineFactoryBean
import org.radeox.engine.context.BaseInitialRenderContext

// Place your Spring DSL code here
beans = {
    textCache(EhCacheFactoryBean) {
        timeToLive = 10000        
    }
    downloadCache(EhCacheFactoryBean) {
        timeToLive = 10000
    }
    contentCache(EhCacheFactoryBean) {
        timeToLive = 5000
    }
    wikiCache(EhCacheFactoryBean) {
        timeToLive = 5000
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