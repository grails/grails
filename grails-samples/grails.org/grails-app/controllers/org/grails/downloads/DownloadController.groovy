package org.grails.downloads

class DownloadController {
    
    def index = { redirect(action:list,params:params) }

    def latest = {

        def downloads = Download.list(max:1, order:'desc', sort:'releaseDate')
        def download = downloads ? downloads[0] : null

        render(view:'index', model:[download:download])    
    }

    def archive = {
        def downloads = Download.findAllBySoftwareName(params.id, [order:'desc', sort:'releaseDate'])

        return [downloads:downloads]
    }


    def downloadFile = {

        def mirror = params.mirror? Mirror.lock(params.mirror) : null
        if(mirror) {

            def download = mirror.file.download
            download.count++
            download.save(flush:true)

            redirect(url:mirror.url)
        }
        else {
            response.sendError 404
        }
    }

    def fileDetails = {
        def downloadFile = DownloadFile.get(params.id)
        [downloadFile:downloadFile]
    }
    def addFile = { AddFileCommand cmd ->
        def download = Download.get(params.id)
        if(request.method == 'POST') {
            if(!cmd.url) {                 
                return [download:download, message:"Invalid URL"]
            }
            else if(!cmd.name) {
                return [download:download, message:"You must specify the name of the mirror"]
            }
            else {
                def downloadFile = new DownloadFile(params)
                downloadFile.addToMirrors(url:cmd.url, name:cmd.name)
                download.addToFiles(downloadFile)
                download.save()
                redirect(action:'show', id:download.id)
            }
        }
        return [download:download]

    }

    def deleteMirror = {
        def mirror = Mirror.get(params.id)
        if(mirror) {
            mirror.delete(flush:true)
            render(template:'mirrorList', model:[downloadFile:mirror.file])
        }
        else {
            render "Mirror not found for id ${params.id}"
        }
    }

    def addMirror = {
        def downloadFile = DownloadFile.get(params.id)

        if(downloadFile) {
            def mirror = new Mirror(params)
            if(mirror.url) {
                downloadFile.addToMirrors(mirror)
                downloadFile.save(flush:true)
                render(template:'mirrorList', model:[downloadFile:downloadFile])   
            }
            else {
                render "Invalid URL specified"
            }
        }
        else {
            render "File not found for id ${params.id}"
        }
    }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
        [ downloadList: Download.list( params ) ]
    }

    def show = {
        def download = Download.get( params.id )

        if(!download) {
            flash.message = "Download not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ download : download ] }
    }

    def delete = {
        def download = Download.get( params.id )
        if(download) {
            download.delete()
            flash.message = "Download ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "Download not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def download = Download.get( params.id )

        if(!download) {
            flash.message = "Download not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ download : download ]
        }
    }

    def update = {
        def download = Download.get( params.id )
        if(download) {
            download.properties = params
            if(!download.hasErrors() && download.save()) {
                flash.message = "Download ${params.id} updated"
                redirect(action:show,id:download.id)
            }
            else {
                render(view:'edit',model:[download:download])
            }
        }
        else {
            flash.message = "Download not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def download = new Download()
        download.properties = params
        return ['download':download]
    }

    def save = {
        def download = new Download(params)
        if(!download.hasErrors() && download.save()) {
            flash.message = "Download ${download.id} created"
            redirect(action:show,id:download.id)
        }
        else {
            render(view:'create',model:[download:download])
        }
    }
}
class AddFileCommand {
    URL url
    String name
}