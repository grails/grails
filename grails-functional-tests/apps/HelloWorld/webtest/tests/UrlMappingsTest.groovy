class UrlMappingsTest extends grails.util.WebTest {
    void suite() {
        testParamsClearedBetweenMappingEvaluations()
        testDoubleWildcardMapping()
        testDoubleWildcardReverseMapping()
        testMultipleVariablesPerPathElement()
    }

    /**
     * Regression test for GRAILS-3369. Checks that the request parameters
     * do not get polluted during mapping evaluation such that earlier
     * mappings affect whether later ones match or not.
     */
    void testParamsClearedBetweenMappingEvaluations() {
        webtest("Regression test for GRAILS-3369") {
            invoke     (url: "fr/hello/")
            verifyText ("Hello world!")
        }
    }

    /**
     * Tests that a URL mapping iwth a double wildcard works as expected.
     */
    void testDoubleWildcardMapping() {
        webtest("Test ** forward mapping") {
            invoke     (url:  "feeds/contents/this/is/a/custom/path")
            verifyText (text: "Path: this/is/a/custom/path")
        }
    }

    /**
     * Tests that the reverse URL mapping works with double-wildcard (**)
     * URLs.
     */
    void testDoubleWildcardReverseMapping() {
        webtest("Test ** reverse mapping") {
            invoke     (url:  "feeds/testReverse")
            verifyText (text: "<a href=\"/HelloWorld/feeds/contents/my/file/at/some/place?id=1\"")
        }
    }

}
