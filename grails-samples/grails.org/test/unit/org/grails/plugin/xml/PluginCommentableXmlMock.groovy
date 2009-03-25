package org.grails.plugin.xml
/*
 * author: Matthew Taylor
 */
class PluginCommentableXmlMock {
    static def XML = """<plugin name='commentable' version='0.7.3' grailsVersion='1.1 &gt; *'>
  <author>Graeme Rocher</author>
  <authorEmail>graeme.rocher@springsource.com</authorEmail>
  <title>Commentable Plugin</title>
  <description>\
A plugin that allows you to attach comments to domain classes in a generic manner
</description>
  <documentation>http://grails.org/Commentable+Plugin</documentation>
  <resources>
    <resource>Config</resource>
    <resource>DataSource</resource>
    <resource>org.grails.comments.CommentableController</resource>
    <resource>TestController</resource>
    <resource>org.grails.comments.Comment</resource>
    <resource>org.grails.comments.CommentLink</resource>
    <resource>org.grails.comments.TestEntry</resource>
    <resource>org.grails.comments.TestPoster</resource>
    <resource>org.grails.comments.CommentableService</resource>
    <resource>org.grails.comments.CommentsTagLib</resource>
  </resources>
  <dependencies>
    <plugin name='hibernate' version='1.1 &gt; *' />
  </dependencies>
</plugin>"""
}