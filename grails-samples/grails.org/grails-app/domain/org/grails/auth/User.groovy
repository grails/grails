package org.grails.auth

class User {
    String email
    String login
	String password

    static hasMany = [roles:Role]

    static constraints = {
        email(email:true, unique:true, blank:false)
        login(blank:false, size:5..15)
		password(blank:false, nullable:false)
	}

    static mapping = {
        cache true
    }

    String toString() {
        login
    }


}