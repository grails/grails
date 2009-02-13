import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.transaction.support.TransactionSynchronizationManager

grailsHome = Ant.project.properties."environment.GRAILS_HOME"

includeTargets << new File("${grailsHome}/scripts/Bootstrap.groovy")

target('default': "Translates all Context objects that represent Plugins into Plugin objects") {
    packageApp()
    classLoader = new URLClassLoader([classesDir.toURL()] as URL[], rootLoader)
    Thread.currentThread().setContextClassLoader(classLoader)
    loadApp()
    configureApp()

    sessionFactory = grailsApp.mainContext.getBean("sessionFactory")
    session = SessionFactoryUtils.getSession(sessionFactory, true)
    TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))

    translateContextToPlugin()
}

target(translateContextToPlugin: "The implementation task") {

    def wikiClass = grailsApp.getDomainClass("org.grails.wiki.WikiPage").clazz

    def id = wikiClass.executeQuery("select max(w.id) from WikiPage w where w.title = 'Plugins'")[0]

    def pluginList = wikiClass.get(id)

    matcher = pluginList.body =~ /\* \[([^\[\]]*)\]/ // matches "* [Plugin name]"

    println "Found ${matcher.size()} plugins within wiki pages... processing..."
    matcher.each {match ->
        def title = match[1].split(/\|/)[0]
        def pluginContent = wikiClass.findByTitle(title)

        if (!pluginContent) {
            def url = match[1].split(/\|/)[1]
            pluginContent = wikiClass.findByTitle(url)
        }

        if (!pluginContent) {
            println "X: ${title} has no docs on grails.org, see: ${match[1].split(/\|/)[1]}"

        } else {
            contentToPlugin pluginContent
        }
    }
}

private contentToPlugin(c) {
    println "--> Translating $c.title to plugin..."
    def p = grailsApp.getDomainClass("org.grails.plugin.Plugin").clazz.newInstance()
    def authors = c.versions*.author
    def author = (authors as Set).inject(null) {mostEdited, it ->
        if (!mostEdited) return it
        if (mostEdited < authors.count(it)) return it
        mostEdited
    }

    p.title = c.title
    p.description = c.body
    p.body = 'see desc'
    p.author = author.login
    p.authorEmail = author.email
    p.documentationUrl = 'not provided'
    p.downloadUrl = 'not provided'
    p.currentRelease = 'not provided'

    if (!p.validate()) {
        p.errors.allErrors.each { println it }
    }
    assert p.save()

    def comment = grailsApp.getDomainClass("org.grails.comment.Comment").clazz.newInstance()
    comment.user = grailsApp.getDomainClass("org.grails.auth.User").clazz.findByLogin('admin')
    comment.body = "This Plugin page was automatically generated.  To see the old Wiki version, go to [${p.title}].  This will only be available for a limited time in order for plugin authors to make adjustments during this transition."
    comment.parent = p
    assert comment.save()
    p.comments = [comment]
    assert p.save()
}
