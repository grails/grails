package com.g2one.blog

class PostController {
    
    def index = { redirect(action:list,params:params) }
    def allowedMethods = [save:'POST']

    def list = {
        [ postList: Post.list( max:5) ]
    }

    def create = {
        [post: new Post(params) ]
    }

    def save = {
        def post = new Post(params)		
        if(!post.hasErrors() && post.save()) {
            flash.message = "Post ${post.id} created"
            redirect(action:list)
        }
        else {
            render(view:'create',model:[post:post])
        }			
    }
}
