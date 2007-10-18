import com.plink.plextile.*; 

files = new File("./src/guide").listFiles().findAll { it.name.endsWith(".textile") }
parser = new TextParser()
ant = new AntBuilder()

book = [:]
for(f in files) {
	def chapter = f.name[0..-9]      
	println "Generating documentation for $chapter"
	def contents = parser.parseTextile(f.text, true)
	book[chapter] = contents
}
    
toc = new StringBuffer()
contents = new StringBuffer()

for(entry in book) {     
	def margin = entry.key.indexOf(' ')
	if(margin <=2) margin = 0
	margin *=5
    toc << "<div class=\"tocItem\" style=\"margin-left:${margin}px\"><a href=\"#${entry.key}\">${entry.key}</a></div>"  	
	contents << "<h2><a name=\"${entry.key}\">${entry.key}</a></h2>"
	contents << entry.value
} 

ant.mkdir(dir:"output")                      
ant.mkdir(dir:"output/img")                      
ant.mkdir(dir:"output/css")   
ant.mkdir(dir:"output/ref")         
ant.copy(file:"resources/style/reference.html",todir:"output")
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
engine = new groovy.text.SimpleTemplateEngine()
new File("./resources/style/layout.html").withReader { reader ->
	template = engine.createTemplate(reader)	
	new File("output/index.html").withWriter { out ->
		template.make(vars).writeTo(out)		
	}
}    

menu = new StringBuffer()
files = new File("src/ref").listFiles()
new File("resources/style/referenceItem.html").withReader { reader ->
	template = engine.createTemplate(reader)		
	for(f in files) {
		if(f.directory && !f.name.startsWith(".")) {
			menu << "<h1 class=\"menuTitle\">${f.name}</h1>"
			new File("output/ref/${f.name}").mkdirs()
			def textiles = f.listFiles().findAll { it.name.endsWith(".textile")}
			def usageFile = new File("src/ref/${f.name}.textile")
			if(usageFile.exists()) {
				def contents = parser.parseTextile(usageFile.text, true)							
				new File("output/ref/${f.name}/Usage.html").withWriter { out ->
				 	template.make(content:contents).writeTo(out)
				}				
				menu << "<div class=\"menuUsageItem\"><a href=\"${f.name}/Usage.html\" target=\"mainFrame\">Usage</a></div>"								
			}
			for(txt in textiles) {                        
				def name = txt.name[0..-9]
				menu << "<div class=\"menuItem\"><a href=\"${f.name}/${name}.html\" target=\"mainFrame\">${name}</a></div>"
				def contents = parser.parseTextile(txt.text, true)			
				println "Generating reference item: ${name}"
				new File("output/ref/${f.name}/${name}.html").withWriter { out ->
				 	template.make(content:contents).writeTo(out)
				}
			}
		}
	}        
	
}
vars.menu = menu
new File("./resources/style/menu.html").withReader { reader ->
	template = engine.createTemplate(reader)	
	new File("output/ref/menu.html").withWriter { out ->
		template.make(vars).writeTo(out)		
	}
}
println "Done. Look at output/index.html"


