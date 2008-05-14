import org.springframework.cache.ehcache.EhCacheFactoryBean
import org.grails.content.notifications.ContentAlertStack

// Place your Spring DSL code here
beans = {    
    contentCache(EhCacheFactoryBean) {
        timeToLive = 5000
    }
    wikiCache(EhCacheFactoryBean) {
        timeToLive = 5000
    }
    contentToMessage(ContentAlertStack)

}