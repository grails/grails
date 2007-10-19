import com.plink.plextile.*;    
import com.lowagie.text.*;
import com.lowagie.text.rtf.*;
import com.lowagie.text.pdf.PdfWriter;
        
title = "The Grails Framework"
version = "1.0-RC1"
authors = "Graeme Rocher, Guillaume LaForge, Steven Devijver"

files = new File("./src/guide").listFiles().findAll { it.name.endsWith(".textile") }
parser = new TextParser()
ant = new AntBuilder()

book = [:]
for(f in files) {
	def chapter = f.name[0..-9]      
	println "Generating documentation for $chapter"
	book[chapter] = f.text
}
    
toc = new StringBuffer()
contents = new StringBuffer()

for(entry in book) {     
	def margin = entry.key.indexOf(' ')
	if(margin <=2) margin = 0
	margin *=5
    toc << "<div class=\"tocItem\" style=\"margin-left:${margin}px\"><a href=\"#${entry.key}\">${entry.key}</a></div>"  	
	contents << "<h2><a name=\"${entry.key}\">${entry.key}</a></h2>"
	contents << parser.parseTextile(entry.value, true)
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
engine = new groovy.text.SimpleTemplateEngine()
new File("./resources/style/layout.html").withReader { reader ->
	template = engine.createTemplate(reader)	
	new File("output/guide.html").withWriter { out ->
		template.make(vars).writeTo(out)		
	}
}    

menu = new StringBuffer()
files = new File("src/ref").listFiles()   
reference = [:]
new File("resources/style/referenceItem.html").withReader { reader ->
	template = engine.createTemplate(reader)		
	for(f in files) {
		if(f.directory && !f.name.startsWith(".")) {
			def section = f.name
			reference."${section}" = [:]
			menu << "<h1 class=\"menuTitle\">${f.name}</h1>"
			new File("output/ref/${f.name}").mkdirs()
			def textiles = f.listFiles().findAll { it.name.endsWith(".textile")}
			def usageFile = new File("src/ref/${f.name}.textile")
			if(usageFile.exists()) { 
				def data = usageFile.text
				reference."${section}".usage = data				
				def contents = parser.parseTextile(data, true)							
				new File("output/ref/${f.name}/Usage.html").withWriter { out ->
				 	template.make(content:contents).writeTo(out)
				}			   
				menu << "<div class=\"menuUsageItem\"><a href=\"${f.name}/Usage.html\" target=\"mainFrame\">Usage</a></div>"								
			}
			for(txt in textiles) {                        
				def name = txt.name[0..-9]
				menu << "<div class=\"menuItem\"><a href=\"${f.name}/${name}.html\" target=\"mainFrame\">${name}</a></div>"
				def data = txt.text                    
				reference."${section}"."$name" = data								
				def contents = parser.parseTextile(data, true)			
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

// Now generate PDF documentation   

/*pdfParser =new PdfParser();

//Create a new document and set the pdf writer attributes
doc = new Document()                                
try{                                                
	contents = new StringBuffer()
	contents << """
h1. ${title} - Reference Documentation

* *Version* - ${version}
* *Authors* - ${authors}	

"""
	for(entry in book) {
		contents << """ 

h1. ${entry.key} 		


"""
		contents << entry.value
	}

contents << """

h1. Reference

"""	
	for(entry in reference) {
contents << """

h1. ${entry.key}
               

"""		    
		def usage = entry.value?.remove('usage')
		if(usage) {
			contents << usage
		}   
		contents << """

h1. ${entry.key} Reference


"""		
		for(ref in entry.value) {
			contents << ref.value
			contents << """--------------------------------------------------------------------

"""
		}
	}

	println "Writing out PDF doc"
	PdfWriter.getInstance(doc,new FileOutputStream("output/guide.pdf"));			
	println "Writing out RTF doc"
	RtfWriter2.getInstance(doc,new FileOutputStream("output/guide.rtf"));
	doc.open();
	//Write the source to a pdf
	pdfParser.parseTextile(contents.toString(),doc);

} finally {
	doc.close()
}   */


println "Done. Look at output/index.html"


