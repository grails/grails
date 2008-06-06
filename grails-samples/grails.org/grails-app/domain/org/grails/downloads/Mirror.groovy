package org.grails.downloads

class Mirror implements Serializable{
    String name
    URL url
    DownloadFile file

    static constraints = {
        url nullable:false
        name blank:false
    }
}