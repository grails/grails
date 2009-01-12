package com.g2one.gtunes

class Permission implements org.jsecurity.authz.Permission, Serializable{
	
	static belongsTo = [user:User]
	
	boolean implies(org.jsecurity.authz.Permission p) { false }	
}