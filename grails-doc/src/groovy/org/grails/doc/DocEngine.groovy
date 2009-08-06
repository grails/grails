package org.grails.doc

import org.radeox.engine.BaseRenderEngine
import org.radeox.api.engine.WikiRenderEngine
import org.radeox.api.engine.context.InitialRenderContext
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
import java.util.regex.Pattern
import org.radeox.macro.code.JavaCodeFilter
import org.radeox.filter.context.BaseFilterContext
import org.radeox.macro.CodeMacro
import org.radeox.macro.parameter.BaseMacroParameter
import org.radeox.util.Encoder

class DocEngine extends BaseRenderEngine implements WikiRenderEngine {
    static final CONTEXT_PATH = "contextPath"
    static final SOURCE_FILE = "sourceFile"

    static GRAILS_HOME = ""
    static EXTERNAL_DOCS = 	[:]
	static ALIAS = [:]
	static BASEDIR
    static {
	   def ant = new AntBuilder()
	   ant.property(environment:"env")       
	   BASEDIR = System.getProperty("base.dir") ?: '.'
	   GRAILS_HOME = "../grails"
	   new File("${BASEDIR}/resources/doc.properties").withInputStream {
			def props = new Properties()
			props.load(it)
			props.findAll { it.key.startsWith("api.")}.each {
				EXTERNAL_DOCS[it.key[4..-1]] = it.value
			}
			props.findAll { it.key.startsWith("alias.")}.each {
				ALIAS[it.key[6..-1]] = it.value
			}			
	   }
    }
	
    DocEngine(InitialRenderContext context) { super(context) }


    boolean exists(String name) {
        int barIndex = name.indexOf('|')
        if(barIndex >-1) {
            def refItem = name[0..barIndex-1]
            def refCategory = name[barIndex+1..-1]


            if(refCategory.startsWith("http://")) return true
            else if(refCategory.startsWith("guide:")) {
				def alias = refCategory[6..-1]
				

				if(ALIAS[alias]) {
					alias = ALIAS[alias]
				}
                def ref = "${BASEDIR}/src/guide/${alias}.gdoc"
                def file = new File(ref)
                if(file.exists()) {
                    return true 
                }
                else {
                    emitWarning(name,ref,"page")
                }
            }
			else if(refCategory.startsWith("api:")) {
				def ref = refCategory[4..-1]
				if(EXTERNAL_DOCS.keySet().find { ref.startsWith(it) }) {
					return true
				}
				ref = ref.replace('.' as char, '/' as char)
				if(ref.indexOf('#') > -1) {
					ref = ref[0..ref.indexOf("#")-1]
				} 
				ref = "${GRAILS_HOME}/doc/api/${ref}.html"
				def file = new File(ref)
                if(file.exists()) {
                    return true 
                }
                else {
                    emitWarning(name,ref,"class")
                }
			}
            else {
                String dir = getNaturalName(refCategory)
                def ref = "${BASEDIR}/src/ref/${dir}/${refItem}.gdoc"
                File file = new File(ref)
                if(file.exists()) {
                    return true
                }
                else {
                    emitWarning(name,ref,"page")
                }
            }
        }

         return false

    }

    private void emitWarning(String name, String ref, String type) {
        println "WARNING: ${initialContext.get(SOURCE_FILE)}: Link '$name' refers to non-existant $type $ref!"
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
                    SourceMacro macro = new SourceMacro(GRAILS_HOME) 
                    macro.setInitialContext(getInitialRenderContext()) 
                    loader.add(repository, macro)
                }
            }
            fp.init();

        }

    }

    void appendLink(StringBuffer buffer, String name, String view, String anchor) {
        def contextPath = initialContext.get(CONTEXT_PATH)

        if(name.startsWith("guide:")) {
			def alias = name[6..-1]
			if(ALIAS[alias]) {
				alias = ALIAS[alias]
			}
	
            buffer <<  "<a href=\"$contextPath/guide/single.html#${alias}\" class=\"guide\">$view</a>"
        }
		else if(name.startsWith("api:")) {
			def link = name[4..-1]

			def externalKey = EXTERNAL_DOCS.keySet().find { link.startsWith(it) }								
			link =link.replace('.' as char, '/' as char) + ".html"	
			
			if(externalKey) {
				buffer <<  "<a href=\"${EXTERNAL_DOCS[externalKey]}/$link${anchor ? '#' + anchor : ''}\" class=\"api\">$view</a>"
			}
			else {
				buffer <<  "<a href=\"$contextPath/api/$link${anchor ? '#' + anchor : ''}\" class=\"api\">$view</a>"				
			}
		}
        else {
            String dir = getNaturalName(name)
			def link = "$contextPath/ref/${dir}/${view}.html"
            buffer <<  "<a href=\"$link\" class=\"$name\">$view</a>"
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
    static final nameCache = [:]
    String getNaturalName(String name) { 
	    if(nameCache[name]) {
			return nameCache[name]
     	}
		else {
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

	        nameCache[name] = words.join(' ')
			return nameCache[name]			
		}
    }
}
public class SourceMacro extends BaseMacro {

    String base

    public SourceMacro(basedir) {
        super();
        this.base = basedir
    }

    String getName() { "source" }
    void execute(Writer out, MacroParameter params) {
        def source = params.params.get("0")




        def i = source.indexOf('=')
        def type = source[0..i-1]
        def name = source[i+1..-1]
        String code

        switch(type) {
            case "tag":
                def j = name.indexOf('.')
                def className = name[0..j-1]
                def tagName = name[j+1..-1]
                Pattern regex = ~/(?s)(\s*?def\s+?$tagName\s*?=\s*?\{\s*?attrs\s*?,{0,1}\s*?(body){0,1}\s*?->.+?)(\/\*\*|def\s*[a-zA-Z]+?\s*=\s*\{)/
                def text = new File("${base}/src/groovy/org/codehaus/groovy/grails/plugins/web/taglib/${className}.groovy").text
                def matcher = regex.matcher(text)
                if(matcher.find()) {
                    out << '<p><a href="#" onclick="document.getElementById(\''+tagName+'\').style.display=\'inline\'">Show Source</a></p>'
                    out << "<div id=\"$tagName\" style=\"display:none;\">"
                    text =  Encoder.escape(matcher.group(1))

                   def macro = new CodeMacro()
                   macro.setInitialContext(this.initialContext)
                   def macroParams = new BaseMacroParameter()
                   macroParams.content = text
                   macro.execute(out, macroParams)
                   out << "</div>"
                }
            break
        }
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
        super(/@([^\n]*?)@/);
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
        super(/!([^\n]*?\.(jpg|png|gif))!/);
    }


    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
        def img = result.group(1)
        if(img.startsWith("http://")) {
            buffer << "<img border=\"0\" class=\"center\" src=\"$img\"></img>"
        }
        else {            
            def path = context.renderContext.get("contextPath") ?: "."
            buffer << "<img border=\"0\" class=\"center\" src=\"$path/img/$img\"></img>"
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

