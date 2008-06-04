package org.grails.downloads

import grails.test.GroovyPagesTestCase

class DownloadTagLibTests extends GroovyPagesTestCase {


    void testLinkTag() {
        def download = new Download(softwareName:"Grails", softwareVersion:"1.0")
        def file = new DownloadFile(title:"Binary Zip")
        file.addToMirrors(name:"Codehaus", url:"http://codehaus.org/grails-1.0.3-bin.zip")
        file.addToMirrors(name:"Grails.org", url:"http://grails.org/grails-1.0.3-bin.zip")
        download.addToFiles(file)

        file = new DownloadFile(title:"Source Zip")
        file.addToMirrors(name:"Codehaus", url:"http://codehaus.org/grails-1.0.3-src.zip")
        file.addToMirrors(name:"Grails.org", url:"http://grails.org/grails-1.0.3-src.zip")
        download.addToFiles(file)

        assert download.save(flush:true)


        def template = '<download:link software="Grails" version="1.0" file="binary zip">download here</download:link>'

        assertOutputEquals('<a href="/download/file?mirror=1">download here</a>', template)

        def mirror = Mirror.get(1)

        assertEquals "http://codehaus.org/grails-1.0.3-bin.zip", mirror.url.toString()
        
    }

}