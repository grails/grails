package com.g2one.cache

class CacheTagLib {
	static namespace = "cache"
	
	CacheService cacheService
	def text = { attrs, body -> 
		def cacheKey = attrs.key
		out << cacheService.cacheOrReturn(cacheKey) {
			body()
		}
	}	
}