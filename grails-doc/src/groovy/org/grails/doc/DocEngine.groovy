package org.grails.doc

import org.radeox.engine.BaseRenderEngine
import org.radeox.api.engine.WikiRenderEngine
import org.grails.doc.filters.HeaderFilter
import org.grails.doc.filters.LinkTestFilter
import org.grails.doc.filters.ListFilter
import org.radeox.filter.*
import org.radeox.filter.regex.RegexTokenFilter
import org.radeox.regex.MatchResult
import org.radeox.filter.context.FilterContext
import org.radeox.macro.parameter.MacroParameter
import org.radeox.macro.BaseMacro
import org.radeox.filter.regex.RegexFilter
import org.radeox.macro.MacroLoader

class DocEngine extends BaseRenderEngine implements WikiRenderEngine {
    String contextPath = "."
    static GRAILS_HOME = ""

    static {
	   def ant = new AntBuilder()
	   ant.property(environment:"env")       
	   GRAILS_HOME = ant.antProject.properties."env.GRAILS_HOME"	
    }
	


    boolean exists(String name) {
        int barIndex = name.indexOf('|')
        if(barIndex >-1) {
            def refItem = name[0..barIndex-1]
            def refCategory = name[barIndex+1..-1]


            if(refCategory.startsWith("http://")) return true
            else if(refCategory.startsWith("guide:")) {
                def ref = "src/guide/${refCategory[6..-1]}.gdoc"
                def file = new File(ref)
                if(file.exists()) {
                   return true 
                }
                else {
                   println "WARNING: Link $name refers to non-existant page $ref!"
                }
            }
			else if(refCategory.startsWith("api:")) {
				def ref = refCategory[4..-1]
				ref = ref.replace('.' as char, '/' as char) + ".html"
				ref = "${GRAILS_HOME}/doc/api/$ref"
				def file = new File(ref)
                if(file.exists()) {
                   return true 
                }
                else {
                   println "WARNING: Javadoc Link $name refers to non-existant class ${ref}!"
                }				
			}
            else {

                String dir = getNaturalName(refCategory)
                def ref = "src/ref/${dir}/${refItem}.gdoc"
                File file = new File(ref)
                if(file.exists()) {
                    return true
                }
                else {
                    println "WARNING: Link $name refers to non-existant page $ref!"
                }
            }
        }

         return false

    }
    boolean showCreate() { false }

    protected void init() {
        if (null == fp) {
          fp = new FilterPipe(initialContext);

            def filters = [
                            ParamFilter,
                            MacroFilter,
                            TextileLinkFilter,
                            HeaderFilter,
                            BlockQuoteFilter,
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
                fp.addFilter(filter)
                
                if(filter instanceof MacroFilter) {
                    MacroLoader loader = new MacroLoader()
                    def repository = filter.getMacroRepository()                
                    loader.add(repository, new WarningMacro())
                    loader.add(repository, new NoteMacro())
                }
            }
            fp.init();

        }

    }

    void appendLink(StringBuffer buffer, String name, String view, String anchor) {
        if(name.startsWith("guide:")) {
            buffer <<  "<a href=\"$contextPath/guide.html#${name[0..6]}\" class=\"$name\">$view</a>"
        }
		else if(name.startsWith("api:")) {
			def link = name[4..-1]
			link =link.replace('.' as char, '/' as char) + ".html"
			
			buffer <<  "<a href=\"$contextPath/api/$link\" class=\"$name[0..3]\">$view</a>"
		}
        else {
            String dir = getNaturalName(name)
            buffer <<  "<a href=\"$contextPath/ref/${dir}/${view}.html\" class=\"$name\">$view</a>"
        }
    }

    void appendLink(StringBuffer buffer, String name, String view) {
        appendLink(buffer,name,view,"")
    }

    void appendCreateLink(StringBuffer buffer, String name, String view) {
        buffer.append(name)
    }

    /**
     * Converts a property name into its natural language equivalent eg ('firstName' becomes 'First Name')
     * @param name The property name to convert
     * @return The converted property name
     */
    String getNaturalName(String name) {
        List words = new ArrayList();
        int i = 0;
        char[] chars = name.toCharArray();
        for (int j = 0; j < chars.length; j++) {
            char c = chars[j];
            String w;
            if(i >= words.size()) {
                w = "";
                words.add(i, w);
            }
            else {
                w = (String)words.get(i);
            }

            if(Character.isLowerCase(c) || Character.isDigit(c)) {
                if(Character.isLowerCase(c) && w.length() == 0)
                    c = Character.toUpperCase(c);
                else if(w.length() > 1 && Character.isUpperCase(w.charAt(w.length() - 1)) ) {
                    w = "";
                    words.add(++i,w);
                }

                words.set(i, w + c);
            }
            else if(Character.isUpperCase(c)) {
                if((i == 0 && w.length() == 0) || Character.isUpperCase(w.charAt(w.length() - 1)) ) 	{
                    words.set(i, w + c);
                }
                else {
                    words.add(++i, String.valueOf(c));
                }
            }

        }

        words.join(' ')
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
class BlockQuoteFilter extends RegexTokenFilter {
    public BlockQuoteFilter() {
        super(/(?m)^bc.\s*?(.*?)\n\n/);
    }
    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
        buffer << "<pre class=\"bq\"><code>${result.group(1)}</code></pre>\n\n"
    }

}
class ItalicFilter extends RegexTokenFilter {
    public ItalicFilter() {
        super(/_([^\n]*?)_/);
    }
    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
        buffer << "<em class=\"italic\">${result.group(1)}</em>"
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
        super(/(\{\{|@)([^\n]*?)(\}\}|@)/);
    }


    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
        buffer << "<code>${result.group(2)}</code>"
    }
}
class ImageFilter  extends RegexTokenFilter {

    public ImageFilter() {
        super(/!([^\n]*?)!/);
    }


    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
        buffer << "<img border=\"0\" src=\"${result.group(1)}\"></img>"
    }
}
class TextileLinkFilter extends RegexTokenFilter {

    public TextileLinkFilter() {
        super(/"([^"]+?)":(.+?)(\s)/);
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

