package com.g2one.gtunes

class Artist {
    String name
    static hasMany = [albums:Album]
    
}
