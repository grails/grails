package org.grails.plugin
/*
 * author: Matthew Taylor
 */

import grails.test.GrailsUnitTestCase
import org.grails.rateable.Rating
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class PluginTests extends GrailsUnitTestCase {

    def cachedConfig 
    
    protected void setUp() {
        super.setUp();
        mockDomain(Plugin)
        cachedConfig = ConfigurationHolder.config
    }
    
    protected void tearDown() {
        ConfigurationHolder.config = cachedConfig
    }

    void testCurrentReleaseValidation() {
        def releaseMap = [
                '1.1': true,
                '1.4.6': true,
                '6.34.667.1': true,
                '2.5-SNAPSHOT': true,
                'steve':false,
                '2.steve.5': false,
                '.3.4.5': false
        ]

        releaseMap.each { version, expected ->
            println "$version $expected"
            def plugin = new Plugin(
                name:'plugin1',
                title: 'stuff',
                authorEmail: 'email', documentationUrl: '', downloadUrl: '',
                currentRelease: version
            )
            def result = plugin.validate()
            if (!result) {
                plugin.errors.allErrors.each { println it }
            }
            println "result $result"
            assertEquals "$version was ${(expected ? '' : 'not ')}expected to validate.", expected, result
        }
    }

    void testTitleConstraintIsLessStringent() {
        def title = 'Dynamically filter / search domain objects.'

        def x = 'a', up = 'A'

        mockForConstraintsTests(Plugin)

        def plugin = new Plugin(
                    name: "plugin-${x}",
                    title: title,
                    description: "hosted at www.${x}-plugin.org",
                    body: "hosted at www.${x}-plugin.org",
                    author: "Peter ${up}. Jackson",
                    authorEmail: "peter_${x}@jackson.com",
                    documentationUrl: "http://www.grails.org/Plugin+${up}+Plugin",
                    downloadUrl: "http://www.${x}-plugin.org/plugin-${x}-5.0.2.zip",
                    currentRelease: "5.0.2"
            )
        assertTrue plugin.validate()
    }

    void testIsNewerThan() {
        def p = new Plugin(currentRelease:'1.0.1')
        p.pluginService = new PluginService()
        assertTrue p.isNewerThan('1.0')
        assertTrue p.isNewerThan('1.1-SNAPSHOT')
        assertFalse p.isNewerThan('1.1')
        assertFalse p.isNewerThan('1.0.2')
    }
    
    void testFisheyeTransientProperty() {
        def config = new ConfigObject()
        config.plugins.fisheye = 'fisheyeUrl'
        ConfigurationHolder.config = config
        def p = new Plugin(name:'kevin', downloadUrl:'something')
        assertEquals "fisheyeUrl/grails-kevin", p.fisheye
    }
}