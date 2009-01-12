package com.g2one.gtunes

class StreamingTagLib {
	
	static namespace = "media"
	
	def player = { attrs, body ->
		def userAgent = request.getHeader('User-Agent')
		def src = attrs.src
		def width = attrs.width ?: 100
		def height = attrs.height ?: 100		
		def autoplay = attrs.autoplay ?: false
		out.write """
<OBJECT CLASSID=\"clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B\" WIDTH=\"${width}\" HEIGHT=\"${height}\"
CODEBASE=\"http://www.apple.com/qtactivex/qtplugin.cab\">
<PARAM name=\"SRC\" VALUE=\"${src}\">
<PARAM name=\"AUTOPLAY\" VALUE=\"${autoplay}\">
<EMBED SRC=\"${src}\" WIDTH=\"${width}\" HEIGHT=\"${height}\" AUTOPLAY=\"${autoplay}\" CONTROLLER=\"true\" LOOP=\"false\" PLUGINSPAGE=\"http://www.apple.com/quicktime/download/\">
</EMBED>
</OBJECT>"""
	}
}