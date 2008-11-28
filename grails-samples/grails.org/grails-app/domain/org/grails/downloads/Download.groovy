package org.grails.downloads

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class Download implements Serializable{
    Date releaseDate = new Date()                    
    String softwareName
    String softwareVersion
    int count
	Boolean betaRelease = false

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