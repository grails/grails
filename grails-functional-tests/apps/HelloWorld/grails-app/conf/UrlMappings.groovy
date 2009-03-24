class UrlMappings {
    static mappings = {
        "/mapping"(controller: "hello", action: "mapping") {
            something = "test string"
        }

        "/mapping2" {
            controller = "hello"
            action = "mapping"
            something = "test string"
        }

        "/$controller/$action?/$id?"{
            something = "test string"
            
            constraints {
                // apply constraints here
            }
        }

        // GRAILS-3112
        "/feeds/contents/$path**"{
            controller = "feeds"
            action = "contents"
        }

        // GRAILS-3087
        "/files/download/$filename.$ext" (controller: "file", action: "get")
        "/3087/prefix-$stem/$action-$id?"(controller: "stem")

        // The next two are related to GRAILS-3369. If Grails is not
        // working correctly, the "example" action remains in the
        // params map for the web request when the next mapping is
        // evaluated. Since the "hello" controller does not have such
        // an action, the next mapping does not match even though it
        // should.
        "/$controller/hello/$id?" {
            action = "example"
        }

        "/$lang/$controller/$action?" {
            constraints {
                lang("matches": /[a-z]{2}/)
            }
        }
        
        "500"(controller: "errors", action: "show") {
            code = 1001
        }

        "414"(view: "/missing")

        "444"(controller: "hello", view: "kaput")
    }
}
