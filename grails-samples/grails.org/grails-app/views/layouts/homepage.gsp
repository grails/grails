<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <g:javascript src="common/application.js"/>
    <g:javascript src="common/tracking.js"/>
    
    <link rel="stylesheet" href="${createLinkTo(dir: 'css/new', file: 'master.css')}" type="text/css" />
    <link rel="stylesheet" href="${createLinkTo(dir: 'css/new', file: 'homepage.css')}" type="text/css" />
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
    <div id="springSourceBar">
		<div id="springSourceLogo">
    		<a href="http://www.springsource.com/"><img src="${resource(dir:'images/new', file:'springsource-logo.jpg')}" border="0"></a>			
		</div>
    </div>
    <div class="mainMenuBarWrapper">
        <ul id="mainMenuBar">
            <li><a href="http://www.springsource.com/products">Products</a></li>
            <li><a href="http://www.springsource.com/services">Support and Services</a></li>
            <li><a href="http://www.springsource.com/training">Training</a></li>
            <li><a href="http://www.springsource.com/customer/casestudies">Case Studies</a></li>
            <li><g:link controller="content" id="Download">Downloads</g:link></li>
            <li><g:link controller="content" id="Documentation">Documentation</g:link></li>
        </ul><!-- mainMenuBar -->
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
                    <h4>June 17-19, 2009</h4>
                    <p>Groovy and Grails - Belgium, Brussels</p>
                    <h4>June 17-19, 2009</h4>
                    <p>Groovy and Grails - Belgium, Brussels</p>
                    <h4>June 17-19, 2009</h4>
                    <p>Groovy and Grails - Belgium, Brussels</p>
                    <h4>June 17-19, 2009</h4>
                    <p>Groovy and Grails - Belgium, Brussels</p>
                    <h4>June 17-19, 2009</h4>
                    <p>Groovy and Grails - Belgium, Brussels</p>
                    <h4>June 17-19, 2009</h4>
                    <p>Groovy and Grails - Belgium, Brussels</p>
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
                                    <li><g:link controller="plugin" action="show" params="[name:plugin.name]">${plugin.title}</g:link></li>
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
                                            <h4>${newsItem.title}</h4>
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
            			<h3>Support</h3>
                        <p>SpringSource enterprise software and <a href="http://www.springsource.com/services/enterprisesupport">support subscriptions</a> are the way to get developer and production support for Grails and other SpringSource software products and certified versions of our open source technologies including patches, updates, security fixes, and legal indemnification. 
	<br><br>
	Support plans provide unlimited support incidents across three different service levels ranging from developer support to business-critical 24x7 production support.</p>
                    </div><!-- center column -->
                    <div class="right">
            			<h3>Services</h3>
                        <p>SpringSource <a href="http://www.springsource.com/support/professional-services">consulting services</a> are available to companies that wish to leverage the knowledge and expertise of SpringSource’s Grails technology experts. We provide client-driven engagements that focus on knowledge transfer, not creating dependencies. 
	<br><br>
Our custom professional services are highly flexible to address specific issues such as onsite troubleshooting and other services tailored to suit your Grails needs. 
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
