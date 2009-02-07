package org.grails.plugin
/*
 * author: Matthew Taylor
 */

import grails.test.GrailsUnitTestCase

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
        assertNull plugin.avgRating
        plugin.addToRatings(new Rating(stars:2))
        assertEquals 2, plugin.avgRating
        plugin.addToRatings(new Rating(stars:3))
        assertEquals 2.5, plugin.avgRating
    }
}