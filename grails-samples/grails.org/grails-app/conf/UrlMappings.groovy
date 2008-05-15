class UrlMappings {
    static mappings = {
      "/wiki/latest"(controller:"content", action:"latest")
      "/auth/$action"(controller:"auth")
      "/news/$id"(controller:"news", action:"showNews")
      "/news/create"(controller:"news", action:"createNews")
      "/news/latest"(controller:"news", action:"latest")
      "/search"(controller:"searchable")
      "/upload/$id?"(controller:"content", action:"uploadImage")
      "/register"(controller:"user", action:"register")
      "/login"(controller:"user", action:"login")
      "/logout"(controller:"user", action:"logout")
      "/edit/$id"(controller:"content", action:"editWikiPage")
      "/save/$id"(controller:"content", action:"saveWikiPage")
      "/create/$id"(controller:"content", action:"createWikiPage")
      "/info/$id"(controller:"content", action:"infoWikiPage")
      "/markup/$id"(controller:"content", action:"markupWikiPage")
      "/version/$id/$number"(controller:"content", action:"showWikiVersion")
      "/rollback/$id/$number"(controller:"content", action:"rollbackWikiVersion")
      "/diff/$id/$number/$diff"(controller:"content", action:"diffWikiVersion")
      "/previous/$id/$number"(controller:"content", action:"previousWikiVersion")
      
      "/$id?"(controller:"content")

      "/admin/$controller/$action?"()
      "/admin"(view:"/admin/index")


      "500"(view:'/error')
	}
}
