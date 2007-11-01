import org.radeox.engine.BaseRenderEngine;
import org.radeox.api.engine.*;
import org.radeox.engine.context.BaseRenderContext
import org.grails.doc.DocEngine;

def ant = new AntBuilder()
ant.property(environment:"env")       
GRAILS_HOME = ant.antProject.properties."env.GRAILS_HOME"
CONTEXT_PATH = "contextPath"

props = new Properties()
new File("./resources/doc.properties").withInputStream { input ->
    props.load(input)
}
new File("${GRAILS_HOME}/build.properties").withInputStream { input ->
    props.load(input)
}

title = props.title
version = props."grails.version"
authors = props.author

def compare = [compare: { o1, o2 ->
    def idx1 = o1.name[0..o1.name.indexOf(' ')-1]
    def idx2 = o2.name[0..o2.name.indexOf(' ')-1]
    def nums1 = idx1.split(/\./).findAll { it.trim() != ''}*.toInteger()
    def nums2 = idx2.split(/\./).findAll { it.trim() != ''}*.toInteger()
    def result = 0
    for(i in 0..<nums1.size()) {
                     if(nums2.size() > i) {
                        result = nums1[i].compareTo(nums2[i])
                        if(result != 0)break
                    }
                 }
    result
},
        equals: { false }] as Comparator

files = new File("./src/guide").listFiles()
        .findAll { it.name.endsWith(".gdoc") }
        .sort(compare)
context = new BaseRenderContext();
context.set(CONTEXT_PATH, ".")

ant = new AntBuilder()
cache = [:]

engine = new DocEngine()
templateEngine = new groovy.text.SimpleTemplateEngine()
context.setRenderEngine(engine)

book = [:]
for(f in files) {
	def chapter = f.name[0..-6]
	//println "Generating documentation for $chapter"
	book[chapter] = f.text
}
    
toc = new StringBuffer()
contents = new StringBuffer()

ant.mkdir(dir:"output/guide")         
new File("resources/style/guideItem.html").withReader { reader ->
	template = templateEngine.createTemplate(reader)		
	for(entry in book) {     
		def margin = entry.key.indexOf(' ')
		def header = 2
		if(margin > -1) {
			def index = entry.key[0..margin]
			def tokens = index.split(/\./)
			margin = tokens.size()

			if(margin == 2 && tokens[1].trim().size() == 0) {
				margin = 1
			}
		}

		if(margin <=1) margin = 0
		margin *=10
	    toc << "<div class=\"tocItem\" style=\"margin-left:${margin}px\"><a href=\"#${entry.key}\">${entry.key}</a></div>"  	
		contents << "<h${header}><a name=\"${entry.key}\">${entry.key}</a></h2>"
		def body = engine.render(entry.value, context) 
		contents << body

		new File("output/guide/${entry.key}.html").withWriter { out ->
			template.make(title:entry.key, content:body).writeTo(out)
	    }
	}
}
 

ant.mkdir(dir:"output")                      
ant.mkdir(dir:"output/img")                      
ant.mkdir(dir:"output/css")   
ant.mkdir(dir:"output/ref")         

ant.copy(file:"resources/style/index.html",todir:"output")
ant.copy(todir:"output/img") {
	fileset(dir:"resources/img")
}       
ant.copy(todir:"output/css") {
	fileset(dir:"resources/css")
}                              
ant.copy(todir:"output/ref") {
	fileset(dir:"resources/style/ref")
}                              
                       
vars = [
            title:props.title,
			subtitle:props.subtitle,
			footer:props.footer,
			authors:props.authors,
			version: props."grails.version",
			copyright: props.copyright,
			
			toc:toc.toString(),
			body:contents.toString()
			]

new File("./resources/style/layout.html").withReader { reader ->
	template = templateEngine.createTemplate(reader)	
	new File("output/guide.html").withWriter { out ->
		template.make(vars).writeTo(out)		
	}
}    

menu = new StringBuffer()
files = new File("src/ref").listFiles().toList().sort()   
reference = [:]
new File("resources/style/referenceItem.html").withReader { reader ->
	template = templateEngine.createTemplate(reader)		
	for(f in files) {
		if(f.directory && !f.name.startsWith(".")) {
			def section = f.name
			reference."${section}" = [:]
			menu << "<h1 class=\"menuTitle\">${f.name}</h1>"
			new File("output/ref/${f.name}").mkdirs()
			def textiles = f.listFiles().findAll { it.name.endsWith(".gdoc")}.sort()
			def usageFile = new File("src/ref/${f.name}.gdoc")
			if(usageFile.exists()) { 
				def data = usageFile.text
				reference."${section}".usage = data
			    engine.configureContextPath "../.."
			    context.set(CONTEXT_PATH, "../..")
				def contents = engine.render(data, context)					
				new File("output/ref/${f.name}/Usage.html").withWriter { out ->
				 	template.make(content:contents).writeTo(out)
				}			   
				menu << "<div class=\"menuUsageItem\"><a href=\"${f.name}/Usage.html\" target=\"mainFrame\">Usage</a></div>"								
			}
			for(txt in textiles) {                        
				def name = txt.name[0..-6]
				menu << "<div class=\"menuItem\"><a href=\"${f.name}/${name}.html\" target=\"mainFrame\">${name}</a></div>"
				def data = txt.text                    
				reference."${section}"."$name" = data
				engine.configureContextPath "../.."
				context.set(CONTEXT_PATH, "../..")
				def contents = engine.render(data, context)		
				//println "Generating reference item: ${name}"
				new File("output/ref/${f.name}/${name}.html").withWriter { out ->
				 	template.make(content:contents).writeTo(out)
				}
			}
		}
	}        
	
}
vars.menu = menu
new File("./resources/style/menu.html").withReader { reader ->
	template = templateEngine.createTemplate(reader)	
	new File("output/ref/menu.html").withWriter { out ->
		template.make(vars).writeTo(out)		
	}
}         




println "Done. Look at output/index.html"


