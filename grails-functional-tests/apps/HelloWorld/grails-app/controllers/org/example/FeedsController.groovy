package org.example

/**
 *
 */
class FeedsController {
    def testReverse = {
        render g.link(
                controller: "feeds",
                action: "contents",
                id: 1,
                params: [ path: "/my/file/at/some/place" ])
    }

    def contents = {
        render "Path: ${params['path']}"
    }
}
