package com.g2one.gtunes


class AlbumPermission extends Permission {	
	Album album
	
	boolean implies(org.jsecurity.authz.Permission p) {
		if(p instanceof AlbumPermission) {
			if(album.id == p.album?.id) {
				return true
			}
		}
		return false
	}
	
	String toString() { "Album Permission: ${album}"}
}