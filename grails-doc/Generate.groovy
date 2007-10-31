import org.radeox.engine.BaseRenderEngine;
import org.radeox.api.engine.*;
import org.radeox.engine.context.BaseRenderContext
import org.grails.doc.DocEngine;

        
title = "The Grails Framework"
version = "1.0-RC1"
authors = "Graeme Rocher, Guillaume LaForge, Steven Devijver"

files = new File("./src/guide").listFiles().findAll { it.name.endsWith(".gdoc") }.sort()
context = new BaseRenderContext();

ant = new AntBuilder()
cache = [:]

engine = new DocEngine()
context.setRenderEngine(engine)

book = [:]
for(f in files) {
	def chapter = f.name[0..-6]
	//println "Generating documentation for $chapter"
	book[chapter] = f.text
}
    
toc = new StringBuffer()
contents = new StringBuffer()

for(entry in book) {     
	def margin = entry.key.indexOf(' ')
	def header = 2
	if(margin > -1) {
		def index = entry.key[0..margin]
		header = index.split(/\./).size()
	}
	if(margin <=2) margin = 0
	margin *=5
    toc << "<div class=\"tocItem\" style=\"margin-left:${margin}px\"><a href=\"#${entry.key}\">${entry.key}</a></div>"  	
	contents << "<h${header}><a name=\"${entry.key}\">${entry.key}</a></h2>"
	contents << engine.render(entry.value, context)
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
            title:"Grails",
			version: "1.0-RC2",
			toc:toc.toString(),
			body:contents.toString()
			]
templateEngine = new groovy.text.SimpleTemplateEngine()
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
			    engine.contextPath = "../.."
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
				engine.contextPath = "../.."
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


