import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import grails.util.*
import groovy.sql.Sql

grailsHome = Ant.project.properties."environment.GRAILS_HOME"

includeTargets << new File("${grailsHome}/scripts/Bootstrap.groovy")


target('default': "Migrates old news items into the blog format") {
    packageApp()
    classLoader = new URLClassLoader([classesDir.toURL()] as URL[], rootLoader)
    Thread.currentThread().setContextClassLoader(classLoader)
    loadApp()
    configureApp()

    sessionFactory = grailsApp.mainContext.getBean("sessionFactory")
    session = SessionFactoryUtils.getSession(sessionFactory, true)
    TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))

    migrateNewsData()
}

target(migrateNewsData: "The implementation task") {
    def ds = grailsApp.config.dataSource
    def sql = Sql.newInstance(ds.url, 'root', '', ds.driverClassName)
    def newsClass = 'org.grails.news.NewsItem'
    def versionClass = 'org.grails.content.Version'
    def blogEntryClass = grailsApp.getDomainClass("org.grails.blog.BlogEntry").clazz
    def userClass = grailsApp.getDomainClass("org.grails.auth.User").clazz
    sql.eachRow("select * from content where class = '${newsClass}'") { newsRow ->
        def newsAuthor = userClass.get(newsRow.author_id)
        def newsBlog = blogEntryClass.newInstance(
            title: newsRow.title,
            body: newsRow.body,
            author: newsAuthor.login,
            dateCreated: newsRow.date_created
        )
        if (!newsBlog.validate()) {
            println "DOES NOT VALIDATE!!"
            newsBlog.errors.allErrors.each { println it }
        } else {
            println "News Blog \"${newsBlog.title}\" will validate from user \"${newsBlog.author}\""
        }
    }
}