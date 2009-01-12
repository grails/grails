package com.g2one.gtunes

class AlbumController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 10
        [ albumList: Album.list( params ) ]
    }

    def show = {
        def album = Album.get( params.id )

        if(!album) {
            flash.message = "Album not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ album : album ] }
    }

    def delete = {
        def album = Album.get( params.id )
        if(album) {
            album.delete()
            flash.message = "Album ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "Album not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def album = Album.get( params.id )

        if(!album) {
            flash.message = "Album not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ album : album ]
        }
    }

    def update = {
        def album = Album.get( params.id )
        if(album) {
            album.properties = params
            if(!album.hasErrors() && album.save()) {
                flash.message = "Album ${params.id} updated"
                redirect(action:show,id:album.id)
            }
            else {
                render(view:'edit',model:[album:album])
            }
        }
        else {
            flash.message = "Album not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def album = new Album()
        album.properties = params
        return ['album':album]
    }

    def save = {
        def album = new Album(params)
        if(!album.hasErrors() && album.save()) {
            flash.message = "Album ${album.id} created"
            redirect(action:show,id:album.id)
        }
        else {
            render(view:'create',model:[album:album])
        }
    }
}
