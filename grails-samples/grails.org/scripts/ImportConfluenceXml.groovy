import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU
import org.xml.sax.*
import javax.xml.parsers.SAXParserFactory

grailsHome = Ant.project.properties."environment.GRAILS_HOME"

includeTargets << new File ( "${grailsHome}/scripts/Bootstrap.groovy" )  

target('default': "Allows migration from Confluence by importing from Confluence XML format") {
    packageApp()
    classLoader = new URLClassLoader([classesDir.toURL()] as URL[], rootLoader)
    Thread.currentThread().setContextClassLoader(classLoader)    
    loadApp()
    configureApp()
	
    importConfluenceXML()
}

target(importConfluenceXML: "The implementation task") {
	if(args) {
        def userClass = grailsApp.getDomainClass("org.grails.auth.User").clazz
        def adminUser = userClass.findByLogin("admin")
        if(!adminUser) {
            println "No admin users exists in the database, please run the application first so that the admin user can be setup"
            return
        }
        def file = new File(args.trim())
		if(file.exists()) {
			println "Loading XML, this can take a while..."
			def parser = SAXParserFactory.newInstance().newSAXParser()
			def handler = new SAXConfluenceParser()
			parser.parse(file, handler)
			println "Finished reading XML, importing to app.."
			handler.pages.eachWithIndex { page , i ->
					def wikiPageClass = grailsApp.getDomainClass("org.grails.wiki.WikiPage").clazz
					def wikiPage = wikiPageClass.newInstance(title:page.title, body:handler.bodies[i])
					try {
                        wikiPage.save(flush:true)
                    } catch (Exception e) {
                        println "WARNING: Can't save page ${page.title}"
                        println e.message
                        return
                    }
					if(wikiPage.hasErrors()) {
						println "WARNING: Can't save page ${page.title}"
						wikiPage.errors.allErrors.each { println it }
					} else {
                        def v = wikiPage.createVersion()
                        v.author = adminUser
                        v.save(flush:true)
                    }

            }
			println "Import finished successfully."
		}
		else {
			println "File $args does not exist"
		}
	}
	else {
		println "Please specify confluence XML file"
	}
}
class SAXConfluenceParser extends  org.xml.sax.helpers.DefaultHandler {
	def pages = []
	def bodies = [:]
	def pageMode = false
	def propertyMode = false
	def bodyIdMode = false
	def captureBodyId = false
	def bodyMode = false
	def lastBodyId
	def lastBody = new StringBuffer()
	def captureBody = false
	def lastProperty
	void startElement(String uri, String localName, String qName, Attributes attributes) {
		if(qName == 'object') pageMode = false
		if(bodyIdMode && qName=='id') {
			captureBodyId = true
		}
		else if(bodyMode) {
			if(qName == 'id') {
				captureBodyId = true
			}
			if(qName == 'property' && 'body' == attributes.getValue('name')) {
				captureBody = true
			}
		}
		else if(pageMode) {		
			if(qName == 'property') {
				propertyMode = true
				lastProperty = attributes.getValue('name')
			}
			if(qName == 'collection' && 'bodyContents' == attributes.getValue('name')) {
				bodyIdMode = true
				pageMode = false
			}
		}
		else {
			def className = attributes.getValue('class')		
			if(qName == 'object' && 'Page' == className) {
				pages << [:]
				pageMode = true
			}		
			else if(qName == 'object' && 'BodyContent' == className) {
				bodyMode = true
			}	
		}
	}
	
	void endElement(String namespaceURI, String localName, String qName)  {
		if(bodyMode && captureBody && 'property' == qName) {
			def entry = bodies.find { key,val -> val == lastBodyId}
			if(entry) {
				entry.value = lastBody.toString()
			}
			
			bodyMode = false
			captureBody = false
			lastBodyId = null
			lastBody = new StringBuffer()			
		}
	}
	
	void characters(char[] ch,
	                       int start,
	                       int length) {
		if(propertyMode) {
			propertyMode = false
			if(lastProperty) {
				pages[-1]."$lastProperty" = String.copyValueOf(ch, start, length)
				lastProperty = null
			}
		}
		else if(bodyMode && captureBodyId) {
			lastBodyId = String.copyValueOf(ch, start, length)
			captureBodyId = false
		}
		else if(bodyMode && captureBody) {
			lastBody << String.copyValueOf(ch, start, length)
		}
		else if(bodyIdMode && captureBodyId) {
			def pageIndex = pages.size()-1
			def id = String.copyValueOf(ch, start, length).trim()
			if(id) {
				bodies[pageIndex] = id
				bodyIdMode = false
				captureBodyId = false
				pageMode=true				
			}
		}
	}
}