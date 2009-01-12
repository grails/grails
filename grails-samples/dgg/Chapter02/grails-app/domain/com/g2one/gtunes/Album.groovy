package com.g2one.gtunes

class Album {
	String title
	static hasMany = [songs:Song]
}
