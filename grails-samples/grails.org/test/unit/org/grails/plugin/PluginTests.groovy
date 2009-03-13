package org.grails.plugin
/*
 * author: Matthew Taylor
 */

import grails.test.GrailsUnitTestCase
import org.grails.rateable.Rating

class PluginTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp();
        mockDomain(Plugin)
    }

    void testTitleIsUnique() {
        def plugin = new Plugin(title:'plugin1', body:'body1')

        plugin = new Plugin(title:'plugin1', body:'body1')
        assertFalse 'should not allow duplicate titles', plugin.validate()
    }

    void testAvgRating() {
        def plugin = new Plugin()
        assertEquals 0,  plugin.avgRating
        plugin.addToRatings(new Rating(stars:2))
        assertEquals 2, plugin.avgRating
        plugin.addToRatings(new Rating(stars:3))
        assertEquals 2.5, plugin.avgRating
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
}