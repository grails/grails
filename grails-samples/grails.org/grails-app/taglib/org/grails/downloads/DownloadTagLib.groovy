package org.grails.downloads

import java.util.concurrent.ConcurrentHashMap

class DownloadTagLib {
    static namespace = "download"

    def link = {  attrs, body ->
        def software = attrs.software
        def version = attrs.version
        def file = attrs.file

        if(software && version && file) {
            def download = Download.findBySoftwareNameAndSoftwareVersion(software, version)
            def files = download ? DownloadFile.findAllByDownload(download) : []  
            
            def mirrors = files?.find { it.title?.equalsIgnoreCase(file) }?.mirrors
            def mirror = mirrors? mirrors[0] : null
            if(mirror) {
                out << g.link(controller:'download', action:'downloadFile', params:[mirror:mirror.id], body())
            }
            else {
                out << "<div style=\"border: 1px solid red\">Download not found for attributes [software=$software, version=$version, file=$file]</div>"    
            }
        }
        else {
            out << '<div style="border: 1px solid red">Tag <download:link> requires attributes "software", "version" and "file" to be specified!</div>'
        }
    }

    private latestCache = new ConcurrentHashMap()
    def latest = { attrs ->
        def software = attrs.software
        def var = attrs.var ?: 'latestDownload'
        if(software) {

            def download = latestCache[software]
            if(!download) {
                def downloads = Download.findAllBySoftwareName(software,[max:1, order:'desc', sort:'releaseDate'])


                if(downloads) {
                    download = downloads[0]
                }
            }
            pageScope[var] = download
        }
    }
}