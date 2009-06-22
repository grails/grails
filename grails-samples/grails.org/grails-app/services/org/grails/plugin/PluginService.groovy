package org.grails.plugin

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.wiki.WikiPage
import org.grails.auth.User
import org.grails.content.Version

class PluginService {

    static int DEFAULT_MAX = 5

    boolean transactional = true
    
    def popularPlugins(minRatings, max = DEFAULT_MAX) {
        def ratingsComparator = new PluginComparator()
        Plugin.list(cache:true, maxResults:max).findAll {
            it.ratings.size() >= minRatings
        }.sort(ratingsComparator).reverse()
    }
    
    def newestPlugins(max = DEFAULT_MAX) {
        Plugin.withCriteria {
            order('dateCreated', 'desc')
            maxResults(max)
			cache true
        }
    }
    
    def runMasterUpdate() {
        translateMasterPlugins(generateMasterPlugins())
    }
    
    def generateMasterPlugins() {
        def pluginLoc = ConfigurationHolder.config?.plugins?.pluginslist
        def listFile = new URL(pluginLoc)
        def listText = listFile.text
        // remove the first line of <?xml blah/>
        listText = listText.replaceAll(/\<\?xml ([^\<\>]*)\>/, '')
        def plugins = new XmlSlurper().parseText(listText)
        
        log.info "Found ${plugins.plugin.size()} master plugins."

        plugins.plugin.inject([]) { pluginsList, pxml ->
            if (!pxml.release.size()) return pluginsList
            def latestRelease = pxml.@'latest-release'
            def latestReleaseNode = pxml.release.find { releaseNode ->
                releaseNode.@version == latestRelease
            }
            def p = new Plugin()
            p.with {
                name = pxml.@name
                grailsVersion = (latestReleaseNode.documentation.toString().startsWith('http://grails.org') ? getGrailsVersion(p) : '')
                title = latestReleaseNode.title.toString() ?: pxml.@name
                description = new WikiPage(body:latestReleaseNode.description.toString() ?: '')
                author = latestReleaseNode.author
                authorEmail = latestReleaseNode.authorEmail
                documentationUrl = replaceOldDocsWithNewIfNecessary(latestReleaseNode.documentation, name)
                downloadUrl = latestReleaseNode.file
                currentRelease = latestRelease
            }

            pluginsList << p
        }
    }

    private def replaceOldDocsWithNewIfNecessary(oldDocs, name) {
        boolean match = oldDocs =~ /http:\/\/(www\.)?grails.org\//
        return match ? "http://grails.org/plugin/${name}" : oldDocs
    }

    def translateMasterPlugins(masters) {
        masters.each { master ->
            def plugin = Plugin.findByName(master.name)
            if (!plugin) {
                // injecting a unique wiki page name for description
                // pull off the desc so we don't try to save it
                def descWiki = master.description
                master.description = null
                // so we need to save the master first to get its id
                if (!master.save()) {
                    log.error "Could not save master plugin: $master.name ($master.title), version $master.currentRelease"
                    master.errors.allErrors.each { log.error "\t$it" }
                    
                }
                // put the wiki page back with a unique title
                descWiki.title = "description-${master.id}"
                master.description = descWiki
                log.info "No existing plugin, creating new ==> ${master.name}"
                // before saving the master, we need to save the description wiki page
                if (!master.description.save() && master.description.hasErrors()) {
                    master.description.errors.allErrors.each { log.error it }
                } else {
                    def v = master.description.createVersion()
                    v.author = User.findByLogin('admin')
                    if(!v.save(flush:true)) {
                        log.warn "Can't save version ${v.title} (${v.number})"
                        v.errors.allErrors.each { log.warn it }
                    }
                }
                //inject dummy wikis for users to fill in
                (Plugin.WIKIS - 'description').each { wiki ->
                    master."$wiki" = new WikiPage(title:"$wiki-${master.id}", body:'')
                    assert master."$wiki".save()
                }
                // give an initial release date of now
                master.lastReleased = new Date()
                // save new master plugin
                if (!master.save()) {
                    log.error "Could not save master plugin: $master.name ($master.title), version $master.currentRelease"
                    master.errors.allErrors.each { log.error "\t$it" }
                } else {
                    log.info "New plugin was saved from master: $master.name"
                    log.info "There are now ${Plugin.count()} plugins."
                }
            } else {
                // update existing plugin
                updatePlugin(plugin, master)
            }
        }
    }

    def updatePlugin(plugin, master) {
        log.info "Updating plugin \"$plugin.name\"..."

        // these attributes are overriden by local plugin domain changes
        updatePluginAttribute('title', plugin, master)
        updatePluginAttribute('author', plugin, master)
        updatePluginAttribute('authorEmail', plugin, master)
        
        // these are always overridden by the master list
        plugin.name = master.name
        plugin.documentationUrl = master.documentationUrl
        plugin.downloadUrl = master.downloadUrl
        // if this was a release update, also update the date of release
        if (plugin.currentRelease != master.currentRelease) {
            plugin.lastReleased = new Date();
        }
        plugin.currentRelease = master.currentRelease
        plugin.grailsVersion = master.grailsVersion

        if (!plugin.save()) {
            log.warn "Local plugin '$plugin.name' was not updated properly... errors follow:"
            plugin.errors.allErrors.each { log.warn it }
        } else {
            def v = plugin.description.createVersion()
            v.author = User.findByLogin('admin')
            if(!v.save(flush:true)) {
                log.warn "Can't save version ${v.title} (${v.number})"
                v.errors.allErrors.each { log.warn it }
            }
        }
        
        log.info "Local plugin '$plugin.name' was updated with master version."
    }
    
    def updatePluginAttribute(propName, plugin, master) {
        if (master."$propName" && !plugin."$propName") {
            plugin."$propName" = master."$propName"
        }
    }

    def resolvePossiblePlugin(wiki) {
        // WikiPages that are actually components of a Plugin should be treated as a Plugin
        if (wiki.title.matches(/(${Plugin.WIKIS.join('|')})-[0-9]*/)) {
            // we're returning the actual parent Plugin object instead of the WikiPage, but we'll make the body
            // of the WikiPage available on this Plugin object so the view can render it as if it were a real
            // WikiPage by calling on the 'body' attributed
            def plugin = Plugin.read(wiki.title.split('-')[1].toLong())
            if (!plugin) {
                log.warn "There should be a plugin with id ${wiki.title.split('-')[1]} to match WikiPage ${wiki.title}, but there is not."
                return null
            }
            plugin.metaClass.getBody = { -> wiki.body }
            return plugin
        }
        wiki
    }

    def compareVersions(v1, v2) {
        def v1Num = new PluginVersion(version:v1)
        def v2Num = new PluginVersion(version:v2)
        v1Num.compareTo(v2Num)
    }

    def getGrailsVersion(plugin) {
        def xmlLoc = "${ConfigurationHolder.config?.plugins?.location}/grails-${plugin.name}/tags/LATEST_RELEASE/plugin.xml"
        def xmlUrl = new URL(xmlLoc)
        def xmlText = xmlUrl.text

        def pluginXml = new XmlSlurper().parseText(xmlText)
        pluginXml.@grailsVersion.toString()
    }

}

class PluginVersion implements Comparable {

    String[] version
    String tag

    public void setVersion(versionString) {
        def split = versionString.split(/[-|_]/)
        version = split[0].split(/\./)
        tag = split.size() > 1 ? split[1] : ''
    }

    public int compareTo(Object o) {
        def result = null
        version.eachWithIndex { versionElem, i ->
            // skip if we've already found a result in a previous index
            if (result != null) return

            // if this version is a snapshot and the other is not, the other is always greater
            if (tag && !o.tag) {
                result = -1
                return
            }
            
            // if the other is a snapshot and this is not, this version is always greater
            if (o.tag && !tag) {
                result = 1
                return
            }

            // make other version 0 if there really is no placeholder for it
            def otherVersion = (o.version.size() == i) ? 0 : o.version[i]

            if (versionElem > otherVersion) {
                result = 1
                return
            }
            if (versionElem < otherVersion) {
                result = -1
                return
            }
        }
        // if the comparison is equal at this point, and there are more elements on the other version, then that version
        // will be greater because it has another digit on it, otherwise the two really are equal
        if (result == null) {
            if (o.version.size() > version.size()) result = -1
            else result = 0
        }
        result
    }   
}

// sorts by averageRating, then number of votes
class PluginComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        if (o1.averageRating > o2.averageRating) return 1
        if (o1.averageRating < o2.averageRating) return -1
        // averateRatings are same, so use number of votes
        if (o1.ratings.size() > o2.ratings.size()) return 1
        if (o1.ratings.size() < o2.ratings.size()) return -1
        return 0
    }
}