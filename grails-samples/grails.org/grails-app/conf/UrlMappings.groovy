class UrlMappings {
    static mappings = {
        "/Download"(controller: "download", action: "latest")
        "/download/file"(controller: "download", action: "downloadFile")
        "/download/archive/$id"(controller: "download", action: "archive")
        "/wiki/latest"(controller: "content", action: "latest")
        "/auth/$action"(controller: "auth")

        "/news/$id"(controller: "news", action: "showNews")
        "/news/create"(controller: "news", action: "createNews")
        "/news/edit/$id"(controller: "news", action: "editNews")
        "/news/latest"(controller: "news", action: "latest")
        
        "/Plugins"(controller: "plugin", action: "index")
        "/plugins"(controller: "plugin", action: "index")
        "/plugin/$name"(controller: "plugin", action:"show")
        "/plugin/home"(controller: "plugin", action:"home")
        "/plugin/search"(controller: "plugin", action:"search")
        "/plugin/list"(controller: "plugin", action:"list")
        "/plugin/create"(controller: "plugin", action:"createPlugin")
        "/plugin/delete/$name"(controller: "plugin", action:"deletePlugin")
        "/plugin/edit/$id"(controller: "plugin", action:"editPlugin")
        "/plugin/addTag/$id"(controller: "plugin", action:"addTag")
        "/plugin/removeTag/$id"(controller: "plugin", action:"removeTag")
        "/plugin/showTag"(controller: 'plugin', action:'showTag')
        "/plugin/postComment/$id"(controller: "plugin", action:"postComment")
        "/plugin/latest"(controller: "plugin", action: "latest")
		"/plugin/category/$category"(controller:"plugin", action:"home")

        "/plugin/showComment/$id"(controller: 'plugin', action:'showComment')

        "/content/postComment/$id"(controller: "content", action:"postComment")

        "/blog/delete/$id"(controller: 'blogEntry', action:'delete')
		"/blog"(controller:"blog", action:"list")
		"/Grails+Screencasts"(controller:"screencast", action:"list")		

        "/rateable/rate/$id"(controller: "rateable", action:"rate")
        "/tag/autoCompleteNames"(controller:'tag', action:'autoCompleteNames')

        "/search"(controller: "content", action: "search")
        "/upload/$id?"(controller: "content", action: "uploadImage")
        "/register"(controller: "user", action: "register")
        "/login"(controller: "user", action: "login")
        "/profile"(controller: "user", action: "profile")
        "/logout"(controller: "user", action: "logout")
        "/edit/$id"(controller: "content", action: "editWikiPage")
        "/save/$id"(controller: "content", action: "saveWikiPage")
        "/preview/$id"(controller: "content", action: "previewWikiPage")
        "/create/$id"(controller: "content", action: "createWikiPage")
        "/info/$id"(controller: "content", action: "infoWikiPage")
        "/markup/$id"(controller: "content", action: "markupWikiPage")
        "/version/$id/$number"(controller: "content", action: "showWikiVersion")
        "/rollback/$id/$number"(controller: "content", action: "rollbackWikiVersion")
        "/diff/$id/$number/$diff"(controller: "content", action: "diffWikiVersion")
        "/previous/$id/$number"(controller: "content", action: "previousWikiVersion")
		
		"/screencasts"(controller:"screencast", action:"list") 
		"/screencast/save"(controller:"screencast", action:"save") 
		"/screencast/search"(controller:"screencast", action:"search") 		
		"/screencast/update"(controller:"screencast", action:"update") 				
		"/screencast/edit/$id"(controller:"screencast", action:"edit") 				
		"/screencast/feed"(controller:"screencast", action:"feed") 						
		"/screencast/add"(controller:"screencast", action:"create")
		"/screencast/show/$id"(controller:"screencast", action:"show")		
		"/comment/add"(controller:"commentable", action:"add")

        "/$id?"(controller: "content", action: "index")

        "/admin/$controller/$action?/$id?"()
        "/admin"(view: "/admin/index")


        "500"(view: '/error')
    }
}
