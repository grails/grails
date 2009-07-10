import org.radeox.engine.BaseRenderEngine;
import org.radeox.api.engine.*;
import org.radeox.engine.context.BaseInitialRenderContext
import org.grails.doc.DocEngine;

def ant = new AntBuilder()

BASEDIR = System.getProperty("base.dir") ?: '.'
GRAILS_HOME = "../grails"
CONTEXT_PATH = DocEngine.CONTEXT_PATH
SOURCE_FILE = DocEngine.SOURCE_FILE

props = new Properties()
new File("${BASEDIR}/resources/doc.properties").withInputStream {input ->
    props.load(input)
}
new File("${GRAILS_HOME}/build.properties").withInputStream {input ->
    props.load(input)
}

title = props.title
version = props."grails.version"
authors = props.author

def compare = [compare: {o1, o2 ->
    def idx1 = o1.name[0..o1.name.indexOf(' ') - 1]
    def idx2 = o2.name[0..o2.name.indexOf(' ') - 1]
    def nums1 = idx1.split(/\./).findAll { it.trim() != ''}*.toInteger()
    def nums2 = idx2.split(/\./).findAll { it.trim() != ''}*.toInteger()
    // pad out with zeros to ensure accurate comparison
    while (nums1.size() < nums2.size()) {
        nums1 << 0
    }
    while (nums2.size() < nums1.size()) {
        nums2 << 0
    }
    def result = 0
    for (i in 0..<nums1.size()) {
        result = nums1[i].compareTo(nums2[i])
        if (result != 0) break
    }        
    result
},
        equals: { false }] as Comparator

files = new File("${BASEDIR}/src/guide").listFiles().findAll { it.name.endsWith(".gdoc") }.sort(compare)
context = new BaseInitialRenderContext();
context.set(CONTEXT_PATH, "..")

ant = new AntBuilder()
cache = [:]

engine = new DocEngine(context)
templateEngine = new groovy.text.SimpleTemplateEngine()
context.setRenderEngine(engine)

book = [:]
for (f in files) {
    def chapter = f.name[0..-6]
    book[chapter] = f
}

toc = new StringBuffer()
soloToc = new StringBuffer()
fullContents = new StringBuffer()
chapterContents = new StringBuffer()
chapterTitle = null

void writeChapter(String title, StringBuffer content) {
    new File("${BASEDIR}/output/guide/${title}.html").withWriter {
        template.make(title: title, content: content.toString()).writeTo(it)
    }
    content.delete(0, content.size()) // clear buffer
}

ant.mkdir(dir: "${BASEDIR}/output/guide")
ant.mkdir(dir: "${BASEDIR}/output/guide/pages")
new File("${BASEDIR}/resources/style/guideItem.html").withReader {reader ->
    template = templateEngine.createTemplate(reader)

    for (entry in book) {
        //println "Generating documentation for $entry.key"
        def title = entry.key
        def level = 0
        def matcher = (title =~ /^(\S+?)\.? /) // drops last '.' of "xx.yy. "
        if (matcher.find()) {
            level = matcher.group(1).split(/\./).size() - 1
        }
        def margin = level * 10

        if (level == 0) {
            if (chapterTitle) // initially null, to collect sections
                writeChapter(chapterTitle, chapterContents)

            chapterTitle = title // after previous used to write prev chapter

            soloToc << "<div class=\"tocItem\" style=\"margin-left:${margin}px\"><a href=\"${chapterTitle}.html\">${chapterTitle}</a></div>"
        }
        else {
            soloToc << "<div class=\"tocItem\" style=\"margin-left:${margin}px\"><a href=\"${chapterTitle}.html#${entry.key}\">${entry.key}</a></div>"
        }        // level 0=h1, (1..n)=h2


        def hLevel = level == 0 ? 1 : 2
        def header = "<h$hLevel><a name=\"${title}\">${title}</a></h$hLevel>"

        context.set(SOURCE_FILE, entry.value)
        context.set(CONTEXT_PATH, "..")
        def body = engine.render(entry.value.text, context)

        toc << "<div class=\"tocItem\" style=\"margin-left:${margin}px\"><a href=\"#${title}\">${title}</a></div>"
        fullContents << header << body
        chapterContents << header << body

        new File("${BASEDIR}/output/guide/pages/${title}.html").withWriter {
            template.make(title: title, content: body).writeTo(it)
        }
    }
}
if (chapterTitle) // write final chapter collected (if any seen)
    writeChapter(chapterTitle, chapterContents)


ant.mkdir(dir: "${BASEDIR}/output")
ant.mkdir(dir: "${BASEDIR}/output/img")
ant.mkdir(dir: "${BASEDIR}/output/css")
ant.mkdir(dir: "${BASEDIR}/output/ref")

ant.copy(file: "${BASEDIR}/resources/style/index.html", todir: "${BASEDIR}/output")
ant.copy(todir: "${BASEDIR}/output/img") {
    fileset(dir: "${BASEDIR}/resources/img")
}
ant.copy(todir: "${BASEDIR}/output/css") {
    fileset(dir: "${BASEDIR}/resources/css")
}
ant.copy(todir: "${BASEDIR}/output/ref") {
    fileset(dir: "${BASEDIR}/resources/style/ref")
}

vars = [
        title: props.title,
        subtitle: props.subtitle,
        footer: props.footer,
        authors: props.authors,
        version: props."grails.version",
        copyright: props.copyright,

        toc: toc.toString(),
        body: fullContents.toString()
]

new File("${BASEDIR}/resources/style/layout.html").withReader {reader ->
    template = templateEngine.createTemplate(reader)
    new File("${BASEDIR}/output/guide/single.html").withWriter {out ->
        template.make(vars).writeTo(out)
    }
    vars.toc = soloToc
    vars.body = ""
    new File("${BASEDIR}/output/guide/index.html").withWriter {out ->
        template.make(vars).writeTo(out)
    }
}

menu = new StringBuffer()
files = new File("${BASEDIR}/src/ref").listFiles().toList().sort()
reference = [:]
new File("${BASEDIR}/resources/style/referenceItem.html").withReader {reader ->
    template = templateEngine.createTemplate(reader)
    for (f in files) {
        if (f.directory && !f.name.startsWith(".")) {
            def section = f.name
            reference."${section}" = [:]
            menu << "<h1 class=\"menuTitle\">${f.name}</h1>"
            new File("${BASEDIR}/output/ref/${f.name}").mkdirs()
            def textiles = f.listFiles().findAll { it.name.endsWith(".gdoc")}.sort()
            def usageFile = new File("src/ref/${f.name}.gdoc")
            if (usageFile.exists()) {
                def data = usageFile.text
                reference."${section}".usage = data
                context.set(SOURCE_FILE, usageFile.name)
                context.set(CONTEXT_PATH, "../..")
                def contents = engine.render(data, context)
                new File("${BASEDIR}/output/ref/${f.name}/Usage.html").withWriter {out ->
                    template.make(content: contents).writeTo(out)
                }
                menu << "<div class=\"menuUsageItem\"><a href=\"${f.name}/Usage.html\" target=\"mainFrame\">Usage</a></div>"
            }
            for (txt in textiles) {
                def name = txt.name[0..-6]
                menu << "<div class=\"menuItem\"><a href=\"${f.name}/${name}.html\" target=\"mainFrame\">${name}</a></div>"
                def data = txt.text
                reference."${section}"."$name" = data
                context.set(SOURCE_FILE, txt.name)
                context.set(CONTEXT_PATH, "../..")
                def contents = engine.render(data, context)
                //println "Generating reference item: ${name}"
                new File("${BASEDIR}/output/ref/${f.name}/${name}.html").withWriter {out ->
                    template.make(content: contents).writeTo(out)
                }
            }
        }
    }

}
vars.menu = menu
new File("${BASEDIR}/resources/style/menu.html").withReader {reader ->
    template = templateEngine.createTemplate(reader)
    new File("${BASEDIR}/output/ref/menu.html").withWriter {out ->
        template.make(vars).writeTo(out)
    }
}




println "Done. Look at output/index.html"


