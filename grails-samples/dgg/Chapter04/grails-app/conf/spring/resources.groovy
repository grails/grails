// Place your Spring DSL code here
beans = {
    albumArtCache(org.springframework.cache.ehcache.EhCacheFactoryBean) {
		timeToLive = 300
	}
}