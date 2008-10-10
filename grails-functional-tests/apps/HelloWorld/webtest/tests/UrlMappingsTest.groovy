class UrlMappingsTest extends grails.util.WebTest {
    void suite() {
        testDoubleWildcardMapping()
        testDoubleWildcardReverseMapping()
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
