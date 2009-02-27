package org.grails.plugin
/*
 * author: Matthew Taylor
 */
class Tag {
    String name
    static hasMany = [plugins:Plugin]
    static belongsTo = Plugin

    void setName(String name) {
        this.@name = name.toLowerCase()
    }

    String toString() {
        name
    }
}