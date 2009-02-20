import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.codehaus.groovy.grails.commons.ConfigurationHolder

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

    // getting the plugin listing wiki page to parse
    def wikiClass = grailsApp.getDomainClass("org.grails.wiki.WikiPage").clazz
    def id = wikiClass.executeQuery("select max(w.id) from WikiPage w where w.title = 'Plugins'")[0]
    def pluginList = wikiClass.get(id)

    def pluginCategories
    pluginList.body.eachLine { line ->
        if (line.contains('h2.')) {
            pluginCategories = (line - 'h2.').trim().trim().split('/')
            pluginCategories = pluginCategories.collect { cat ->
                def result = cat - 'plugins'
                result = result - 'plugin'
                result = result - 'Plugins'
                result = result - 'Plugin'
                result.trim()
            }
            println ">> plugin categories: $pluginCategories"
        } else {
            matcher = line =~ /\* \[([^\[\]]*)\]/ // matches "* [Plugin name]"
            matcher.each {match ->
                def title = match[1].split(/\|/)[0]
                println ">>>> ${title} in ${pluginCategories}"
                def pluginContent = wikiClass.findByTitle(title)

                if (!pluginContent) {
                    println " ! There was no Wiki Page with title '${title}'"
                    def url = match[1].split(/\|/)[1]
                    println " ->Trying by url: ${url}"
                    pluginContent = wikiClass.findByTitle(url)
                }

                if (!pluginContent) {
                    println "X: ${title} has no docs on grails.org, see: ${match[1].split(/\|/)[1]}"
                } else {
                    contentToPlugin pluginContent, pluginCategories
                }
            }
        }
    }

}

private contentToPlugin(c, tagNames) {
    println "--> Translating $c.title to plugin..."
    def adminUser = grailsApp.getDomainClass("org.grails.auth.User").clazz.findByLogin('admin')
    def pluginClass = grailsApp.getDomainClass("org.grails.plugin.Plugin").clazz
    def wikiClass = grailsApp.getDomainClass("org.grails.wiki.WikiPage").clazz
    def tagClass = grailsApp.getDomainClass("org.grails.plugin.Tag").clazz
    def p = pluginClass.newInstance()
    def authors = c.versions*.author
    def author = (authors as Set).inject(null) {mostEdited, it ->
        if (!mostEdited) return it
        if (mostEdited < authors.count(it)) return it
        mostEdited
    }

    // there will not be a name, so we'll try to guess one
    matcher = c.body =~ /(?<=\binstall-plugin\s)(\w+)(-)?(\w+)[ |\n|\}|\']/
    def name = matcher ? matcher[0][0].toString() : null
    if (name && !pluginClass.executeQuery("from Plugin p where p.name = '${name[0..-2]}'")) {
        // strip off the last char, as the regex will leave it
        p.name = name[0..-2]
        println "--> I think ${c.title} is '$p.name'..."
    } else {
        def newName = 'fix-this-' + c.title.toLowerCase().replaceAll(/\s+/, '-')
        println "!! Cannot procure a plugin name for \"${c.title}\" !!"
        println "\t\"${c.title}\" will be called ${newName} for now."
        p.name = newName
    }

    // check to see if there is already something in the database to remove first
    def existing = pluginClass.executeQuery("from Plugin p where p.title = '${c.title}'")
    existing.each { it
        println "Removing existing plugins before saving '$p.name'"
        it.comments.each { cmt -> cmt.delete() }
        it.description?.delete()
        it.delete(flush:true)
    }

    p.title = c.title

    p.author = author.login
    p.authorEmail = author.email
    p.documentationUrl = 'not provided'
    p.downloadUrl = 'not provided'
    p.currentRelease = 'not provided'

    if (!p.validate()) {
        println "!! ERROR saving plugins ${p}!!"
        p.errors.allErrors.each { println it }
    }

    assert p.save(flush:true)

    // needed to save the plugin before adding the description wiki page, because we need to identify the plugin by id
    // within the wiki description to ensure it is unique
    def descWiki = wikiClass.newInstance()
    descWiki.title = "description-${p.id}"
    descWiki.body = c.body
    p.description = descWiki
    if (!p.description.validate()) {
        println "!! ERROR saving pluging description wiki for ${p}!!"
        p.description.errors.allErrors.each { println it }
    }
    assert p.description.save()

    def v = p.description.createVersion()
    v.author = adminUser
    try {
        v.save(flush:true)
    } catch (Exception e) {
        println "WARNING: Can't save version ${v.title} (${v.number})"
    }   

    p.tags = []
    p.save(flush:true)

    // working with the tag names provided to add tags to new plugins appropriately
    tagNames.each { tagName ->
        def tag = tagClass.executeQuery("from Tag t where t.name = '${tagName.toLowerCase()}'")[0]
        if (!tag) {
            tag = tagClass.newInstance()
            tag.name = tagName
            assert tag.save(flush:true)
            println " * created new tag $tag ($tag.id) * "
        }
        p.tags << tag
        println "Added tag $tag to $p"
    }
    assert p.save(flush:true)

    // adding a comment to the plugin about this move
    def comment = grailsApp.getDomainClass("org.grails.comment.Comment").clazz.newInstance()
    comment.user = adminUser
    comment.body = """This Plugin page was automatically generated.  To see the old Wiki version, go to [${p.title}]. \
This will only be available for a limited time in order for plugin authors to make adjustments during this \
transition."""
    if (!comment.validate()) {
        println "!! ERROR adding Comment to plugin: ${p}!!"
        comment.errors.allErrors.each { println it }
    }
    assert comment.save(flush:true)
    p.comments = [comment]
    p.save(flush:true)

    // add a comment to the original content object before locking it
    comment = grailsApp.getDomainClass("org.grails.comment.Comment").clazz.newInstance()
    comment.user = adminUser
    comment.body = """This wiki page has been locked because another page outdates it.  Please see the new plugin page \
for "${c.title}" [here|${ConfigurationHolder.config.grails.serverURL}/plugin/${p.name}]."""
    if (!comment.validate()) {
        println "!! ERROR adding Comment to content: ${c}!!"
        comment.errors.allErrors.each { println it }
    }
    assert comment.save()
    if (c.comments) c.comments << comment
    else c.comments = [comment]
    if (!c.validate()) {
        println "!! ERROR saving content after comment: ${c}!!"
        c.errors.allErrors.each { println it }
    }
    c.locked = true
    assert c.save(flush:true)
}
