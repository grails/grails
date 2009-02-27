package org.grails.plugin

import org.grails.auth.User
/*
 * author: Matthew Taylor
 */
class Rating {
    static final def STARS = [1..5]
    int stars
    User user
    static belongsTo = [plugin:Plugin]

    static constraints = {
        stars range:1..5
    }
}