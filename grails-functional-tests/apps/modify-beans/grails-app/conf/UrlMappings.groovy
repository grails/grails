class UrlMappings {
    static mappings = {
        "/$controller/$action?/$id?" {}
        "/"(controller: "home")
        "500"(view:'/error')
    }
}
