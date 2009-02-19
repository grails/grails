package org.grails.plugin

import org.grails.auth.User
/*
 * author: Matthew Taylor
 */
class Rating {
    static final def STARS = [1..5]
    int stars
    User user
}