dataSource {
	pooled = true
	url = "jdbc:mysql://localhost/gtunes_ch16"
	driverClassName = "com.mysql.jdbc.Driver" 
	username = "gtunes"
	password = "password"
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='com.opensymphony.oscache.hibernate.OSCacheProvider'
}
// environment specific settings
environments {
	development {
		dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
		}
	}
	test {
		dataSource {
			dbCreate = "create-drop"
			driverClassName = "org.hsqldb.jdbcDriver"			
			url = "jdbc:hsqldb:mem:testDb"
			username = "sa"			
			password = ""
		}
	}
	production {
		dataSource {
			dbCreate = "update"
		}
	}
}