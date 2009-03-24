/**
 * Controller designed to test GRAILS-3087.
 */
public class StemController {
    def list = {
        render contentType: "text/html",
               text: g.link(action: "show", params: [stem: "fixed"], id: params.id) { "Link for stem ${params.stem}" }
    }

    def show = {
        render "Show - ID = ${params.id} - Stem = ${params.stem}"
    }
}
