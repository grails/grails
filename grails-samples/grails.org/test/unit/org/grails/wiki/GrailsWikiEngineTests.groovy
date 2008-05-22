package org.grails.wiki

import org.radeox.engine.context.BaseInitialRenderContext

/**
 * @author Graeme Rocher
 * @since 1.0
 * 
 * Created: May 19, 2008
 */
class GrailsWikiEngineTests extends GroovyTestCase{

   GrailsWikiEngine engine
    def context

    protected void setUp() {
        context = new BaseInitialRenderContext();
        engine = new GrailsWikiEngine(context)

        
        context.setRenderEngine engine
        enablePageExists()
    }

    def enableNotFound() {
        WikiPage.metaClass.static.findByTitle = { String s-> null}
    }

    def enablePageExists() {
        WikiPage.metaClass.static.findByTitle = { String s-> new WikiPage() }
    }

    void testHeadTags() {

        assertEquals "<a name=\"Hello\"></a><h1>Hello</h1>",engine.render('h1. Hello', context)
    }


    /*void testTables() {
        def text = '''

||Language name||Language code ||
|Java|java |
|Java Script|js, jscript, javascript |
|PHP|php |
|Groovy|gvy, groovy |

'''

        assertEquals '<p class="paragraph"/><table class="wikiTable"><tr class="wikiHeaderRow"><th>Language name</th><th>Language code </th></tr><tr class="wikiRow"><td>Java</td><td>java </td></tr><tr class="wikiRow"><td>Java Script</td><td>js, jscript, javascript </td></tr><tr class="wikiRow"><td>PHP</td><td>php </td></tr><tr class="wikiRow"><td>Groovy</td><td>gvy, groovy </td></tr></table>',engine.render(text, context)

    } */

    void testPageLinksWithAnchors() {
         assertEquals 'My Link <a href="/Test+Page#MyAnchor" class="pageLink">Test Page</a>',engine.render('My Link [Test Page|Test Page#MyAnchor]', context)
    }

    void testAnchorLinks() {
         assertEquals '<a href="#MyAnchor" class="pageLink">MyAnchor</a>',engine.render('[#MyAnchor]', context)
    }

    void testAnchorLinksInBullets() {
         assertEquals '''<ul class="star">
<li><a href="#Abstract" class="pageLink">Abstract</a></li>
</ul>''',engine.render('* [#Abstract]', context)
    }

    void testAnchorMacro() {
         assertEquals '<a name="MyAnchor"></a>some text <a name="Another"></a>',engine.render('{anchor:MyAnchor}some text {anchor:Another}', context)        
    }

    void testAnchorMacroWithHeadTag() {
        assertEquals '<a name="OpenLaszlo plugin"></a><h1><a name="top"></a>OpenLaszlo plugin</h1>', engine.render('h1. {anchor:top}OpenLaszlo plugin', context)
    }

    void testMailToLinks() {
        assertEquals '<a href="mailto:user-unsubscribe@grails.codehaus.org" class="pageLink">unsubscribe</a>', engine.render('[unsubscribe|mailto:user-unsubscribe@grails.codehaus.org]', context)

        enableNotFound()

        assertEquals '<a href="mailto:user-unsubscribe@grails.codehaus.org" class="pageLink">unsubscribe</a>', engine.render('[unsubscribe|mailto:user-unsubscribe@grails.codehaus.org]', context)
    }

    void testAbsoluteLinks() {
        assertEquals '<a href="http://grails.org/unsubscribe" class="pageLink">unsubscribe</a>', engine.render('[unsubscribe|http://grails.org/unsubscribe]', context)
        assertEquals '<a href="http://grails.org/unsubscribe" class="pageLink">http://grails.org/unsubscribe</a>', engine.render('[http://grails.org/unsubscribe]', context)
        enableNotFound()
        assertEquals '<a href="http://grails.org/unsubscribe" class="pageLink">unsubscribe</a>', engine.render('[unsubscribe|http://grails.org/unsubscribe]', context)
        assertEquals '<a href="http://grails.org/unsubscribe" class="pageLink">http://grails.org/unsubscribe</a>', engine.render('[http://grails.org/unsubscribe]', context)
        
    }

    void testImages() {
        def text = '''!image.jpg!

hello world!

an again!
'''
         assertEquals '''<img border="0" class="center" src="./images/image.jpg"></img><p class="paragraph"/>hello world!<p class="paragraph"/>an again!
''', engine.render(text, context)
    }
}

