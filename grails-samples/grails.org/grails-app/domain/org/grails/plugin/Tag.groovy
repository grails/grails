package org.grails.plugin
/*
 * author: Matthew Taylor
 */
class Tag {
    String name
    static hasMany = [plugins:Plugin]
    static belongsTo = Plugin

    static searchable = {
        only = ['name']
    }

    void setName(String name) {
        this.@name = name.toLowerCase()
    }

    String toString() {
        name
    }
}