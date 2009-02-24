package org.grails.plugin

import org.grails.wiki.WikiPage
import org.grails.content.Version
import org.grails.auth.User

class PluginServiceUnitTests extends grails.test.GrailsUnitTestCase {
    def service

    void setUp() {
        super.setUp()
        mockDomain(User, [new User(login:'admin')])
        service = new PluginService()
        service.metaClass.getLog = { ->
            [
                info: { String s -> println s},
                error: { String s -> println s}
            ]
        }
    }

    void testGenerateMasterPlugins() {
        URL.metaClass.constructor = { path ->
            [text:org.grails.plugin.xml.PluginsListXmlMock.PLUGINS_LIST]
        }

        def plugins = service.generateMasterPlugins()
        
        assertNotNull plugins
        assertEquals 3, plugins.size()
        assertEquals 'autobase', plugins[0].name
        assertEquals '0.8.1', plugins[0].currentRelease
        assertEquals 'avatar', plugins[1].name
        assertEquals '0.3', plugins[1].currentRelease
        assertEquals 'yui', plugins[2].name
        assertEquals '2.6.0', plugins[2].currentRelease
        
        def autobase = plugins[0]
        // focus on the rest of the attribs on one plugin
        assertEquals 'Automate your database work as much as possible', autobase.title
        assertEquals 'Robert Fischer', autobase.author
        assertEquals 'robert.fischer@smokejumperit.com', autobase.authorEmail
        assertTrue autobase.description.body.startsWith('This plugin marries the ')
        assertEquals 'http://github.com/RobertFischer/autobase/wikis', autobase.documentationUrl
        assertEquals 'http://plugins.grails.org/grails-autobase/tags/RELEASE_0_8_1/grails-autobase-0.8.1.zip', autobase.downloadUrl
    }
    
    void testTranslateMasterPlugins_AddsPluginsThatDontExist() {
        def mockPluginList = [new Plugin(name: 'mock plugin', title: 'Mock Plugin')]
        mockDomain(Plugin, mockPluginList)
        mockDomain(WikiPage)

        service.translateMasterPlugins(generateMockMasterPluginList())
        // there are 26 masters, and 1 existing, so result should be 27
        assertEquals "Master plugins were not saved properly", 27, mockPluginList.size()
    }
    
    void testTranslateMasterPlugins_DoesntAddPluginThatExists() {
        def mockPluginList = [new Plugin(name: 'plugin-a', title: 'Plugin A Plugin')]
        mockDomain(Plugin, mockPluginList)
        mockDomain(WikiPage)
        mockDomain(Version)

        service.translateMasterPlugins(generateMockMasterPluginList())
        // there are 26 masters, and one existing that should have been updated, so 26 total
        assertEquals "Master plugins were not saved", 26, mockPluginList.size()
    }
    
    void testTranslateMasterPlugins_DoesntAddPluginThatExists_WhenNameIsNull() {
        def mockPluginList = [new Plugin(title: 'Plugin A Plugin')]
        mockDomain(Plugin, mockPluginList)
        mockDomain(WikiPage)
        mockDomain(Version)

        service.translateMasterPlugins(generateMockMasterPluginList())
        // there are 26 masters, and one existing that should have been updated, so 26 total
        assertEquals "Master plugins were not saved", 26, mockPluginList.size()
    }
    
    void testUpdatePlugin() {
        mockDomain(Plugin)
        mockDomain(WikiPage)
        mockDomain(Version)
        def master = generateMockMasterPluginList()
        def plugin = new Plugin(
            name: 'plugin-a', 
        )

        service.updatePlugin(plugin, master[0])
        assertEquals 'plugin-a', plugin.name
        assertEquals 'Plugin A Plugin', plugin.title
        assertEquals "hosted at www.a-plugin.org", plugin.description.body
        assertEquals "Peter A. Jackson", plugin.author
        assertEquals "peter_a@jackson.com", plugin.authorEmail
        assertEquals "http://www.grails.org/Plugin+A+Plugin", plugin.documentationUrl
        assertEquals "http://plugins.grails.org/plugin-a-5.0.2.zip", plugin.downloadUrl
        assertEquals "5.0.2", plugin.currentRelease
    }
    
    void testUpdatePluginOverridesExisting_DocUrl_DlUrl_Release() {
        mockDomain(Plugin)
        mockDomain(WikiPage)
        mockDomain(Version)

        def master = generateMockMasterPluginList()
        def plugin = new Plugin(
            name: 'plugin-a', 
            documentationUrl: 'old doc url',
            downloadUrl: 'old dl url',
            currentRelease: 'old release'
        )
        service.updatePlugin(plugin, master[0])
        assertEquals "http://www.grails.org/Plugin+A+Plugin", plugin.documentationUrl
        assertEquals "http://plugins.grails.org/plugin-a-5.0.2.zip", plugin.downloadUrl
        assertEquals "5.0.2", plugin.currentRelease
    }
    
    void testUpdatePluginDoesNotOverrideExisting_Title_Desc_Body_Author_AuthorEmail() {
        mockDomain(Plugin)
        mockDomain(WikiPage)

        def master = generateMockMasterPluginList()
        def plugin = new Plugin(
            name: 'plugin-a', 
            title: 'Plugin A',
            description: new WikiPage(title:'Description', body:'old description'),
            author: 'Richard D. James',
            authorEmail: 'richard@aphextwin.com'
        )
        service.updatePlugin(plugin, master[0])
        assertEquals "Plugin A", plugin.title
        assertEquals 'old description', plugin.description.body
        assertEquals 'Richard D. James', plugin.author
        assertEquals 'richard@aphextwin.com', plugin.authorEmail
    }
    
    void testRunMasterUpdate() {
        def translated = false
        service.metaClass.generateMasterPlugins = { ->
            println "mock generate"
            'master plugins'
        }
        service.metaClass.translateMasterPlugins = { masters ->
            println "mock translate"
            translated = true
            assertEquals 'master plugins', masters
        }
        service.runMasterUpdate()
        assertTrue 'Master plugins were not translated', translated
    }



    private def generateMockMasterPluginList() {
        ('a'..'z').inject([]) {masterList, x ->
            def up = x.toUpperCase()
            WikiPage descPage = new WikiPage(title: 'Description', body: "hosted at www.${x}-plugin.org")
            descPage.version = 2
            masterList << new Plugin(
                    name: "plugin-${x}",
                    title: "Plugin ${up} Plugin",
                    description: descPage,
                    author: "Peter ${up}. Jackson",
                    authorEmail: "peter_${x}@jackson.com",
                    documentationUrl: "http://www.grails.org/Plugin+${up}+Plugin",
                    downloadUrl: "http://plugins.grails.org/plugin-${x}-5.0.2.zip",
                    currentRelease: "5.0.2"
            )
        }
    }
}
