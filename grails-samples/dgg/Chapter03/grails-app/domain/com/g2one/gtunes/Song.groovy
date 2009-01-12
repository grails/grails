package com.g2one.gtunes

class Song {

    String title
    Integer duration

    static constraints = {
        duration(min:1)
    }
}
