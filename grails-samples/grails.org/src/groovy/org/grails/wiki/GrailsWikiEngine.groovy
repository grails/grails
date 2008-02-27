package org.grails.wiki

import org.radeox.engine.BaseRenderEngine
import org.radeox.api.engine.WikiRenderEngine
import org.grails.wiki.WikiPage
import org.radeox.macro.BaseMacro
import org.radeox.macro.parameter.MacroParameter
import org.radeox.filter.regex.RegexTokenFilter

import org.radeox.filter.context.FilterContext
import org.radeox.regex.MatchResult
import org.radeox.filter.FilterPipe
import org.radeox.filter.regex.RegexFilter
import org.radeox.filter.MacroFilter
import org.radeox.macro.MacroLoader
import org.radeox.api.engine.context.InitialRenderContext
import org.radeox.filter.*
import org.radeox.util.Encoder

/**
* @author Graeme Rocher
* @since 1.0
*
* Created: Feb 19, 2008
*/
class GrailsWikiEngine extends BaseRenderEngine implements WikiRenderEngine{

    static CONTEXT_PATH = "contextPath"
    static CACHE = "cache"

    public GrailsWikiEngine(InitialRenderContext initialRenderContext) {
        super(initialRenderContext);
    }

    public GrailsWikiEngine() {
        super();
    }




    protected void init() {
      if (null == fp) {
          FilterPipe localFP = new FilterPipe(initialContext);
          

            def filters = [
                            ParamFilter,
                            MacroFilter,
                            TextileLinkFilter,
                            HeaderFilter,
                            ListFilter,
                            LineFilter,
                            StrikeThroughFilter,
                            NewlineFilter,
                            ParagraphFilter,
                            BoldFilter,
                            CodeFilter,
                            ItalicFilter,
                            LinkTestFilter,
                            ImageFilter,
                            MarkFilter,
                            KeyFilter,
                            TypographyFilter,
                            EscapeFilter]

            for(f in filters) {
                RegexFilter filter = f.newInstance()
                localFP.addFilter(filter)

                if(filter instanceof MacroFilter) {
                    MacroLoader loader = new MacroLoader()
                    def repository = filter.getMacroRepository()
                    loader.add(repository, new WarningMacro())
                    loader.add(repository, new NoteMacro())
                }
            }
            localFP.init();
            fp = localFP

        }
    }

    
    public boolean exists(String name) {

        def cache = initialContext.get(CACHE)
        if(cache?.getWikiText(name)) return true
        
        WikiPage page = WikiPage.findByTitle(name)

        return page != null
    }

    public boolean showCreate() {
        return true;
    }

    public void appendLink(StringBuffer buffer, String name, String view, String anchor) {
        def contextPath = initialContext.get(CONTEXT_PATH)
        contextPath = contextPath ?: ""

        buffer <<  "<a href=\"$contextPath/$name\" class=\"$name\">$view</a>"
    }

    public void appendLink(StringBuffer buffer, String name, String view) {
        def contextPath = initialContext.get(CONTEXT_PATH)
        contextPath = contextPath ?: ""

        
        buffer <<  "<a href=\"$contextPath/$name\" class=\"$name\">$view</a>"
    }

    public void appendCreateLink(StringBuffer buffer, String name, String view) {
        def contextPath = initialContext.get(CONTEXT_PATH)
        contextPath = contextPath ?: "."
        
        buffer <<  "<a href=\"$contextPath/create/$name\" class=\"$name\">$view</a>"
    }

}

public class WarningMacro extends BaseMacro {
    String getName() {"warning"}
    void execute(Writer writer, MacroParameter params) {
    writer << '<blockquote class="warning">' << params.content << "</blockquote>"
  }
}
public class NoteMacro extends BaseMacro {
    String getName() {"note"}
    void execute(Writer writer, MacroParameter params) {
    writer << '<blockquote class="note">' << params.content << "</blockquote>"
  }
}

class ItalicFilter extends RegexTokenFilter {
    public ItalicFilter() {
        super(/\s_([^\n]*?)_\s/);
    }
    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
        buffer << " <em class=\"italic\">${result.group(1)}</em> "
    }
}
class BoldFilter extends RegexTokenFilter {
    public BoldFilter() {
        super(/\*([^\n]*?)\*/);
    }
    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
        buffer << "<strong class=\"bold\">${result.group(1)}</strong>"
    }
}
class CodeFilter extends RegexTokenFilter {

    public CodeFilter() {
        super(/\s@([^\n]*?)@\s/);
    }


    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
		def text = result.group(1)
		// are we inside a code block?
		if(text.indexOf('class="code"') > -1) buffer << "@$text@"
		else buffer << "<code>${text}</code>"
    }
}
class ImageFilter  extends RegexTokenFilter {
    public ImageFilter() {
        super(/!([^\n]*?)!/);
    }


    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {

        def img = result.group(1)
        def path = context.renderContext.get(GrailsWikiEngine.CONTEXT_PATH) ?: "."
                    

        def image = img.startsWith("http:") ? img :  "$path/images/$img"

        buffer << "<img border=\"0\" class=\"center\" src=\"$image\"></img>"
    }
}
class LinkTestFilter extends RegexTokenFilter {


    public LinkTestFilter() {
        super(/\[(.*?)\]/)
    }

    
    public void handleMatch(StringBuffer buffer, MatchResult matchResult, FilterContext filterContext) {
        def engine = filterContext.getRenderContext().getRenderEngine()

        
        if(engine instanceof WikiRenderEngine) {
            GrailsWikiEngine wikiEngine = engine


            try {
                String name = matchResult.group(1)

                int pipeIndex = name.indexOf('|');
                String alias = name;
                if (-1 != pipeIndex) {
                    alias = name.substring(0, pipeIndex);
                    name = name.substring(pipeIndex + 1);
                }

                def link = name.startsWith("http:") ? name : java.net.URLEncoder.encode(name,'utf-8')

                if(wikiEngine.exists(name) )
                    wikiEngine.appendLink(buffer, link, alias)
                else {
                    wikiEngine.appendCreateLink(buffer, link, alias)
                }
            }
            catch(Exception e) {
                println e.message
                e.printStackTrace()
            }
        }
    }

}
class TextileLinkFilter extends RegexTokenFilter {

    public TextileLinkFilter() {
        super(/"([^"]+?)":(\S+?)(\s)/);
    }


    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
        def text = result.group(1)
        def link = result.group(2)
        def space = result.group(3)

        if(link.startsWith("http://")) {
            buffer << "<a href=\"$link\" target=\"blank\">$text</a>$space"
        }
        else {
            buffer << "<a href=\"$link\">$text</a>$space"
        }
    }
}
class HeaderFilter extends RegexTokenFilter{

    public HeaderFilter() {
        super(/(?m)^h(\d)\.\s+?(.*?)$/);
    }


    public void handleMatch(StringBuffer out, MatchResult matchResult, FilterContext filterContext) {

          def header = matchResult.group(1)
          def content = matchResult.group(2)
          out << "<h$header>$content</h$header>"
    }


}