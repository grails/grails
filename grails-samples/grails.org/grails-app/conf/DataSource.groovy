dataSource {
	pooled = true
	driverClassName = "com.mysql.jdbc.Driver"			
	url = "jdbc:mysql://localhost/grails"
	username = "root"
	password = ""
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
	development {
		dataSource {
			logSql=true			
			dbCreate = "update" // one of 'create', 'create-drop','update'
		}
	}
	test {
		dataSource {
			dbCreate = "create-drop"
			url = "jdbc:hsqldb:mem:testDb"
            driverClassName = "org.hsqldb.jdbcDriver"
            username = "sa"
            password = ""
        }
	}
	production {
		dataSource {
			dbCreate = "update"
			jndiName = 'java:comp/env/jdbc/grailsSiteDS'
		}
	}
}
