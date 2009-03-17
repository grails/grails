import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import grails.util.*

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
    def id = wikiClass.executeQuery("select w.id from WikiPage w where w.title = 'Plugins' order by w.lastUpdated")[0]
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
    def tagClass = grailsApp.getDomainClass("org.grails.taggable.Tag").clazz
    def tagLinkClass = grailsApp.getDomainClass("org.grails.taggable.TagLink").clazz
    def p = pluginClass.newInstance()
    def authors = c.versions*.author
    def mostEditedCount = 0
    def author = (authors as Set).inject(null) {mostEdited, it ->
        if (!mostEdited) {
            mostEditedCount++
            return it
        }
        if (mostEditedCount < authors.count(it)) return it
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
        it.comments.each { existing.removeComment(it) }
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
    def descWiki = wikiClass.newInstance(title:"description-${p.id}", body: c.body)
    p.description = descWiki
    if (!p.description.validate()) {
        println "!! ERROR saving pluging description wiki for ${p}!!"
        p.description.errors.allErrors.each { println it }
    }
    assert p.description.save()
    p.installation = wikiClass.newInstance(title:"installation-${p.id}", body: '')
    assert p.installation.save()
    p.faq = wikiClass.newInstance(title:"faq-${p.id}", body: '')
    assert p.faq.save()
    p.screenshots = wikiClass.newInstance(title:"screenshots-${p.id}", body: '')
    assert p.screenshots.save()

    def v = p.description.createVersion()
    v.author = adminUser
    try {
        v.save(flush:true)
    } catch (Exception e) {
        println "WARNING: Can't save version ${v.title} (${v.number})"
    }   
    p.save(flush:true)

    // working with the tag names provided to add tags to new plugins appropriately
    tagNames.each { tagName ->

        def tag = tagClass.executeQuery("from Tag t where t.name = '${tagName.toLowerCase()}'")[0]
        if (!tag) {
            tag = tagClass.newInstance(name:tagName)
            assert tag.save(flush:true)
            println " * created new tag $tag ($tag.id) * "
        }
        def tagLink = tagLinkClass.newInstance(tag:tag, tagRef:p.id, type:'plugin')
        assert tagLink.save(flush:true)
        println "Added tag $tag to $p"
    }
    if (!p.save(flush:true)) {
        p.errors.allErrors.each { println it }
    }

    println "handling comments..."
    // adding a comment to the plugin about this move
    def text = """This Plugin page was automatically generated.  To see the old Wiki version, go to [${p.title}]. \
This will only be available for a limited time in order for plugin authors to make adjustments during this \
transition."""
    addComment(text, p, adminUser)

    // add a comment to the original content object before locking it
    text = """This wiki page has been locked because another page outdates it.  Please see the new plugin page \
for "${c.title}" [here|${ConfigurationHolder.config.grails.serverURL}/plugin/${p.name}]."""
    addComment(text, c, adminUser)

    println "handling images..."
    // handle any image references
    def context = org.codehaus.groovy.grails.commons.ApplicationHolder.application.parentContext.servletContext
    def imageMatcher = p.description.body =~ /\![a-zA-Z0-9_+\/\.\-\%]+\!/
    imageMatcher.each { match ->
        println "\tFound image reference: $match"
        def relPath = match.toString().replaceAll('!','')
        def existingFile = new File(context.getRealPath("/images/${relPath}"))
        println "\t\tI think I found that image here: ${existingFile.absolutePath}"
        if (!existingFile.exists()) {
            println "\t\tWARNING: ${existingFile.absolutePath} does not exist... trying another place..."
            // try looking in the wiki page name directory
            relPath = "${c.title.encodeAsURL()}/${relPath}"
            existingFile = new File(context.getRealPath("/images/${relPath}"))
            if (!existingFile.exists()) {
                println "\t\tWARNING: ${existingFile.absolutePath} does not exist... I GIVE UP!!"
                return
            }
        }
        def newFileName = relPath.split('/')[-1]
        def newPath = context.getRealPath("/images/${p.description.title.encodeAsURL()}/${newFileName}")
        def newFile = new File("$newPath")
        println "\t\tI'm going to put the new one here: ${newFile.absolutePath}"
        if(!newFile.parentFile.exists()) newFile.parentFile.mkdirs()
        try {
            copyFile(existingFile, newFile)
        } catch (IOException e) {
            println "\t\tERROR: $e"
            return
        }
        // if the image file copy was make properly, we'll need to update the wiki page with the new reference
        def newRelPath = relPath.replace(c.title.encodeAsURL(), p.description.title.encodeAsURL())
        println "\t\tREPLACING ${match.toString()} with !${newRelPath}!"
        p.description.body = p.description.body.replace(match.toString(), "!${newRelPath}!")
    }
    assert p.description.save()
}

private void addComment(text, instance, poster) {
    println "Adding comment..."
    def comment = grailsApp.getDomainClass("org.grails.comments.Comment").clazz.newInstance(body:text, posterId: poster.id, posterClass: poster.class.name)
    println comment
    if (!comment.save(flush:true)) {
        comment.errors.allErrors.each { println it }
    }
    def link = grailsApp.getDomainClass("org.grails.comments.CommentLink").clazz.newInstance(comment:comment, commentRef:instance.id, type:GrailsNameUtils.getPropertyName(instance.class))
    println link
    if (!link.save(flush:true)) {
        link.errors.allErrors.each { println it }   
    }
}

private void copyFile(File source, File destination) {
   def reader = source.newReader()
   destination.withWriter { writer ->
       writer << reader
   }
   reader.close()
}