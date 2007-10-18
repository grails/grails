import com.plink.plextile.*; 

files = new File("./src").listFiles().findAll { it.name.endsWith(".txt") }
parser = new TextParser()
ant = new AntBuilder()

book = [:]
for(f in files) {
	def chapter = f.name[0..-5]      
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
ant.copy(todir:"output/img") {
	fileset(dir:"resources/img")
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
println "Done. Look at output/index.html"


