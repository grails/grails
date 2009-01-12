package com.g2one.cache

import net.sf.ehcache.Ehcache
import net.sf.ehcache.Element

class CacheService {

    static transactional = false
	Ehcache globalCache
	
	def cacheOrReturn(Serializable cacheKey, Closure callable) {
	    def entry = globalCache?.get(cacheKey)?.getValue()
	     if(!entry) {
	          entry = callable.call()
	          globalCache.put new Element(cacheKey, entry)
	      }
	      return entry    	
	}
}
