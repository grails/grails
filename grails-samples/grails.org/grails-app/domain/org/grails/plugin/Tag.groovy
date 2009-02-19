package org.grails.plugin
/*
 * author: Matthew Taylor
 */
class Tag {
    String name

    void setName(String name) {
        this.@name = name.toLowerCase()
    }

    String toString() {
        name
    }
}