package org.grails.cache

import net.sf.ehcache.Ehcache
import net.sf.ehcache.Element

class CacheTagLib {
    static namespace = "cache"

    Ehcache textCache

    def text = { attrs, body ->
        def id  = attrs.id
        if(!id) out << body()
        else {
            def content = textCache?.get(id)?.value
            if(content) out << content
            else {
                content = body()
                textCache?.put(new Element(id, content))
                out << content
            }
        }

    }
}