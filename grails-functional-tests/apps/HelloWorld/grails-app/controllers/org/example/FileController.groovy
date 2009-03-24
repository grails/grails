package org.example

/**
 * Controller for accessing files on the system.
 */
public class FileController {
    def get = {
        def link = g.link(controller: "file", action: "get", params: [filename: "dooby", ext: "pdf"]) {
            "Link to dooby file"
        }

        render """\
<html>
  <body>
    <div>Downloading file '${params.filename}.${params.ext}'...</div>
    <div id="link">
      ${link}
    </div>
  </body>
</html>
"""
    }
}
