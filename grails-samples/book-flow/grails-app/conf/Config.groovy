// log4j configuration
log4j {
    appender.stdout = "org.apache.log4j.ConsoleAppender"
    appender."stdout.layout"="org.apache.log4j.PatternLayout"        
    rootLogger="info,stdout"
    logger {
		grails.spring="info,stdout"            
		org.codehaus.groovy.grails.web="info,stdout"
        org.codehaus.groovy.grails.commons="info,stdout"
        org.codehaus.groovy.grails.plugins="info,stdout"
        org.springframework="off,stdout"     
        org."springframework.webflow"="info,stdout"
        org.codehaus.groovy.grails.orm.hibernate="info,stdout"
		org.hibernate="off,stdout"
    }                      
	additivity.'default' = false
    additivity {  
		grails=false
        org.codehaus.groovy.grails=false
        org.springframework=false 
		org."springframework.webflow" = false
		org.hibernate=false
    }
}