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


}

