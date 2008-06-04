package org.grails.downloads

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class Download {
    Date releaseDate = new Date()                    
    String softwareName
    String softwareVersion
    int count

    List files
    static hasMany = [files:DownloadFile]   

    static trainsients = ['releaseNotes']

    def getReleaseNotes() {
        def config = ConfigurationHolder.getConfig()
        def server = config.grails.serverURL
        return "$server/$softwareVersion+Release+Notes"
    }

    static constraints = {
        softwareName blank:false
        softwareVersion blank:false
        count min:0
    }

    static mapping = {
        count column: '`count`'
    }
}