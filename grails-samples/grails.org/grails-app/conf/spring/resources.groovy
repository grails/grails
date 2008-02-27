import org.springframework.cache.ehcache.EhCacheFactoryBean

// Place your Spring DSL code here
beans = {    
    contentCache(EhCacheFactoryBean) {
        timeToLive = 5000
    }
    wikiCache(EhCacheFactoryBean) {
        timeToLive = 5000
    }
    
}