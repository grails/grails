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
        
        "500"(controller: "errors", action: "show") {
            code = 1001
        }

        "414"(view: "/missing")

        "444"(controller: "hello", view: "kaput")
    }
}
