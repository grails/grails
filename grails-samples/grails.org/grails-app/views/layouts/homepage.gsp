<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <g:javascript src="common/application.js"/>
    <g:javascript src="common/tracking.js"/>
    
    <link rel="stylesheet" href="${resource(dir: 'css/new', file: 'master.css')}" type="text/css" />
    <link rel="stylesheet" href="${resource(dir: 'css/new', file: 'homepage.css')}" type="text/css" />
	<!--[if IE]>
    <link rel="stylesheet" href="${resource(dir: 'css/new', file: 'ie.css')}"/>
	<![endif]-->

	<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
	<link rel="icon" href="/images/favicon.ico" type="image/x-icon">


	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<meta name="robots" content="NOODP">	
	<meta name="Description" content="Grails is a high-productivity web framework based on the Groovy language that embraces the coding by convention paradigm, but is designed specifically for the Java platform.">	
	
	<title>Grails - The search is over.</title>

	<g:layoutHead />

</head>
<body>
    
<div align="center">
	<g:render template="/content/logos"></g:render>
    <div class="mainMenuBarWrapper">
		<g:render template="/content/mainMenuBar" />
    </div><!-- mainMenuBarWrapper -->
</div><!-- center -->

<div id="graphicHeader"></div>

<div id="barDecoration"></div>

<div id="contentWrapper">
    <div id="contentCenter" align="center">
        <div id="contentArea">
            
            <div id="grailsAttributes">
                <div class="left">
        	        <h2>Rapid</h2>
                    <p>Have your next Web 2.0 project done in weeks instead of months. Grails delivers a new age of Java web application productivity.</p>		
                </div><!-- left column -->
                <div class="center">
        			<h2>Dynamic</h2>
                    <p>Get instant feedback, see instant results. Grails is the premier dynamic language web framework for the JVM.</p>
                </div><!-- center column -->
                <div class="right">
        			<h2>Robust</h2>
                    <p>Powered by <a href="http://springframework.org">Spring</a>, Grails out performs the competition. Dynamic, agile web development without compromises.</p>
                </div><!-- right column -->
            </div><!-- grailsAttributes -->
            
            <div class="eventDownloadWrapper">
                <div id="events">
                    <h3>Training Events</h3>
		 			<wiki:text page="Training Events" />
                </div><!-- events -->
                
                <div id="globeGraphic"></div>
            
                <div id="downloadBox">
                    <div class="downloadPluginWrapper">
                        <div id="download">
					        <download:latest var="grailsDownload" software="Grails" />
                            <h3>
								<g:if test="${grailsDownload}">
									<download:link software="Grails" version="${grailsDownload?.softwareVersion}" file="Binary Zip">
										<img src="${resource(dir:'images/new',file:'download_button.png')}" 
											 border="0"
											alt="Download Grails" />
									</download:link>								
								</g:if>
								<g:else>
									<g:link controller="content" id="Download">
									  <img src="${resource(dir:'images/new',file:'download_button.png')}" 
									       border="0"
										   alt="Download Grails" />								
									</g:link>
								</g:else>
							</h3>
							<p>
                            	Download the latest Grails&#8482 binary release to experience a new level of productivity on the Java&#8482 platform.</p>
                            <h4><g:link controller="content" id="Download">More Downloads</g:link></h4>
                        </div><!-- download -->
                        <div id="plugins">
                            <h3>
								<g:link controller="plugin">
									<img src="${resource(dir:'images/new',file:'plugins_button.png')}" alt="Grails Plugins" border="0" />
								</g:link>
								
								
							</h3>
                            <ul>
                                <g:each var="plugin" in="${newestPlugins}">
                                    <li><g:link controller="plugin" action="show" params="[name:plugin.name]"><wiki:shorten text="${plugin.title}" /></g:link></li>
                                </g:each>
                            </ul>
                            <h4><g:link controller="plugin">View All</g:link></h4>
                        </div><!-- plugins -->
                    </div><!-- downloadPluginWrapper -->
                </div><!-- downloadBox -->
            </div><!-- eventDownloadWrapper -->
            
            <div class="newsScreencastWrapper">
                <div id="latestNews">
                    <h2>Latest News</h2>
                    <div id="newsBox">
                        <ul>
                            <g:each var="newsItem" in="${newsItems}">
                                <li>
                                    <g:link controller="blog"  action="showEntry" params="[author:newsItem.author, title: newsItem.title]">
                                        <div class="detail">
                                            <h4><wiki:shorten length="50" text="${newsItem.title}" /></h4>
                                            <div class="author">by ${newsItem.author} | ${newsItem.tags.join(', ')}</div>
                                            <div class="comments">${newsItem.comments.size()} comments</div>
                                        </div>
                                        <div class="calendar">
                                            <div class="month">${newsItem.month}</div>
                                            <div class="day">${newsItem.day}</div>
                                        </div>
                                    </g:link>
                                </li>
                            </g:each>
                        </ul>
                        <div class="actions">
                            <g:link controller="blog" action="home">More news</g:link> |	
                            <g:link controller="blog" action="createEntry">Add news</g:link> | <g:link controller="blog" action="feed" params="[format:'rss']">Subscribe</g:link>
                        </div><!-- actions -->
                    </div><!-- newsBox -->
                </div><!-- latestNews -->
            
                <div id="screencasts">
                    <h2>Recent Screencasts</h2>
                    <div class="castBox">
                        <div class="castScreen">
                            <g:link controller="screencast" action="show" id="${latestScreencastId}">
                                <img src="/images/new/play_icon.png"/>
                            </g:link>
                        </div><!-- castScreen -->
                        <h4><g:link controller="screencast" action="list">View All</g:link></h4>
                    </div><!-- castBox -->
                </div><!-- screencasts -->
            </div><!-- newsScreencastWrapper -->
            
        </div><!-- contentArea -->
    </div><!-- contentCenter -->
    
    <div id="grailsOptionsWrapper">
        <div id="btmSectionGraphicsWrapper">
            <div id="mountainLeft"></div>
            <div id="knight"></div>
            <div id="mountainRight"></div>
            <div id="castle"></div>
        </div><!-- btmSectionGraphicsWrapper-->
        <div id="btmSectionBackgroundStretch">
            <div align="center">
                <div id="grailsOptions">
                    <div class="left">
            	        <h3>Training</h3>
                        <p><a href="http://www.springsource.com/training">SpringSource University</a> provides a comprehensive education and certification program for SpringSource software products as well as Spring, Groovy, Grails, and Apache open source technologies.
	<br><br>
	 We offer both public and customized private training and have trained over 4,000 people. Whichever course you decide to take, you are guaranteed to experience what many before you refer to as “The best Enterprise Software Class I have ever attended”.
						</p>		
                    </div><!-- left column -->
                    <div class="center">
            			<h3>Support &amp; Services</h3>
                        <p>SpringSource enterprise software and <a href="http://www.springsource.com/services/enterprisesupport">support subscriptions</a> are the way to get developer and production support for Grails and other SpringSource software products and certified versions of our open source technologies including patches, updates, security fixes, and legal indemnification. 
	<br><br><a href="http://www.springsource.com/support/professional-services">Consulting services</a> are available to companies that wish to leverage the knowledge and expertise of SpringSource’s Grails technology experts. We provide client-driven engagements that focus on knowledge transfer, not creating dependencies. </p>
                    </div><!-- center column -->
                    <div class="right">
            			<h3>Community</h3>
						<p>
Get involved! Grails has a vibrant and buzzing community. You can grab the <a href="http://github.com/grails/grails/tree/master">source code</a> from Github, report issues on the Grails  <a href="http://jira.codehaus.org/browse/GRAILS">JIRA issue tracker</a>, participate at the <a href="http://grails.org/Mailing+lists">mailing lists</a> or <a href="http://www.nabble.com/grails---user-f11861.html">Nabble forums</a> or catch-up on the latest news on the <g:link controller="blog" action="home">Grails blog</g:link>.							
<br></br><br></br>

The whole Grails site is written in Grails <g:meta name="app.grails.version" /> and is an open wiki,  the <a href="http://github.com/grails/grails/tree/6590710d84f1167031be8399c5cc8ce0b0982ba3/grails-samples/grails.org">source code</a> for which is available from Github.
Visit the Grails <g:link controller="content" id="Community">community pages</g:link> for more ways to participate. 
						</p>
                    </div><!-- right column -->
                </div><!-- grailsOptions -->
            </div><!-- center -->
        </div><!-- btmSectionBackgroundStretch -->
    </div><!-- grailsOptionsWrapper -->
</div><!-- contentWrapper -->

<div id="footer">
    <div align="center">
        <div class="innerFooter">
            <a href="http://twitter.com/grailsframework"><div class="twitter"></div></a>
            <a href="http://www.springsource.com"><div class="springSource"></div></a>
            <p>&copy; Copyright 2009 SpringSource.<br/>All Rights Reserved.</p>
        </div><!-- innerFooter -->
    </div><!-- center -->
</div><!-- footer -->

</body>
</html>
