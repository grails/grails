<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <g:javascript src="jquery/jquery-1.3.2.min.js"/>
    <g:javascript src="common/application.js"/>
    <g:javascript src="common/tracking.js"/>
    
    <link rel="stylesheet" href="css/new/master.css" type="text/css" media="screen" title="Master screen stylesheet" charset="utf-8" />
    <link rel="stylesheet" href="css/new/homepage.css" type="text/css" media="screen" title="Master screen stylesheet" charset="utf-8" />
    
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<meta name="robots" content="NOODP">	
	<meta name="Description" content="Grails is a high-productivity web framework based on the Groovy language that embraces the coding by convention paradigm, but is designed specifically for the Java platform.">	
	
	<title>Grails - The search is over.</title>

	<g:layoutHead />

</head>
<body>
    
<div align="center">
    <a href="http://www.springsource.com/"><div id="springSourceBar"></div></a>
    <div class="mainMenuBarWrapper">
        <ul id="mainMenuBar">
            <li>Products</li>
            <li>Support and Services</li>
            <li>SpringSource University</li>
            <li>News and Events</li>
            <li>Partners</li>
            <li>Exchange</li>
            <li>About Us</li>
        </ul>
    </div>
</div>

<div id="graphicHeader"></div>

<div id="barDecoration"></div>
<div id="contentWrapper">
    <div id="contentCenter" align="center">
        <div id="contentArea">
            
            <div id="grailsAttributes">
                <div class="left">
        	        <h2>Rapid</h2>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ut risus in eros.</p>		
                </div>
                <div class="center">
        			<h2>Dynamic</h2>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ut risus in eros</p>
                </div>
                <div class="right">
        			<h2>Robust</h2>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ut risus in eros.</p>
                </div>
            </div>
            
            <div class="eventDownloadWrapper">
                <div id="events">
                    <h3>Events</h3>
                    <ul>
                        <li>
                            <h4>June 17-19, 2009</h4>
                            Groovy and Grails - Belgium, Brussels
                        </li>
                        <li>
                            <h4>June 17-19, 2009</h4>
                            Groovy and Grails - Belgium, Brussels
                        </li>
                        <li>
                            <h4>June 17-19, 2009</h4>
                            Groovy and Grails - Belgium, Brussels
                        </li>
                        <li>
                            <h4>June 17-19, 2009</h4>
                            Groovy and Grails - Belgium, Brussels
                        </li>
                        <li>
                            <h4>June 17-19, 2009</h4>
                            Groovy and Grails - Belgium, Brussels
                        </li>
                        <li>
                            <h4>June 17-19, 2009</h4>
                            Groovy and Grails - Belgium, Brussels
                        </li>
                    </ul>
                </div>
                
                <div id="globeGraphic"></div>
            
                <div id="downloadBox">
                    <div class="downloadPluginWrapper">
                        <div id="download">
                            <h3>Download Grails v1.1.1</h3>
                            A short description of what I am downloading and some fluffy text highlighting grails key features.  Should not be longer than this.
                            <h4>Learn More</h4>
                        </div>
                        <div id="plugins">
                            <h3>Grails Plugins</h3>
                            <ul>
                                <g:each var="plugin" in="${newestPlugins}">
                                    <li><g:link controller="plugin" action="show" params="[name:plugin.name]">${plugin.title}</g:link></li>
                                </g:each>
                            </ul>
                            <h4>View All</h4>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="newsScreencastWrapper">
                <div id="latestNews">
                    <h2>Latest News</h2>
                    <div id="newsBox">
                        <ul>
                            <li>
                                <div class="detail">
                                    <h4>AmazonFPS Payments Plugin Released</h4>
                                    <div class="author">by someguy | tag1, tag2, tag3</div>
                                    <div class="comments">2 comments</div>
                                </div>
                                <div class="calendar">
                                    <div class="month">June</div>
                                    <div class="day">01</div>
                                </div>
                                
                            </li>
                            <li>
                                <div class="detail">
                                    <h4>AmazonFPS Payments Plugin Released</h4>
                                    <div class="author">by someguy | tag1, tag2, tag3</div>
                                    <div class="comments">2 comments</div>
                                </div>
                                
                                <div class="calendar">
                                    <div class="month">June</div>
                                    <div class="day">01</div>
                                </div>
                                
                            </li>
                            <li>
                                <div class="detail">
                                    <h4>AmazonFPS Payments Plugin Released</h4>
                                    <div class="author">by someguy | tag1, tag2, tag3</div>
                                    <div class="comments">2 comments</div>
                                </div>
                                <div class="calendar">
                                    <div class="month">June</div>
                                    <div class="day">01</div>
                                </div>
                                
                            </li>
                        </ul>
                        <div class="actions">Add news | Subscribe</div>
                    </div>
                </div>
            
                <div id="screencasts">
                    <h2>Recent Screencasts</h2>
                    <div class="castBox">
                        <div class="castScreen">
                            <img src="/images/new/play_icon.png"/>
                        </div>
                        <h4>View All</h4>
                    </div>
                    
                </div>
            </div>
            
        </div>
    </div>
    
    <div id="grailsOptionsWrapper">
        <div id="grailsOptionsGraphicsWrapper">
            <div id="mountainLeft"></div>
            <div id="knight"></div>
            <div id="mountainRight"></div>
            <div id="castle"></div>
        </div>
        <div id="grailsOptionsBackgroundStretch">
            <div align="center">
                <div id="grailsOptions">
                    <div class="left">
            	        <h3>Training</h3>
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ut risus in eros consectetur feugiat. Fusce elementum convallis porttitor. Nulla facilisi. Morbi at erat felis. Aenean ante nisl, pulvinar nec varius ut, egestas sit amet dolor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam vitae fermentum odio. Quisque quis lacus id dolor rhoncus placerat ut eu odio. Duis nec ipsum est. Proin congue faucibus risus, at blandit libero rutrum at. Vivamus ac laoreet lectus. Nam et nisi est. Aliquam hendrerit tristique lectus sit amet fringilla. Aenean pretium lacus vitae massa bibendum feugiat. Suspendisse a mi mauris. Etiam fermentum lacus nec lectus luctus vitae porta nisl ultrices.</p>		
                    </div>
                    <div class="center">
            			<h3>Support</h3>
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ut risus in eros consectetur feugiat. Fusce elementum convallis porttitor. Nulla facilisi. Morbi at erat felis. Aenean ante nisl, pulvinar nec varius ut, egestas sit amet dolor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam vitae fermentum odio. Quisque quis lacus id dolor rhoncus placerat ut eu odio. Duis nec ipsum est. Proin congue faucibus risus, at blandit libero rutrum at. Vivamus ac laoreet lectus. Nam et nisi est. Aliquam hendrerit tristique lectus sit amet fringilla. Aenean pretium lacus vitae massa bibendum feugiat. Suspendisse a mi mauris. Etiam fermentum lacus nec lectus luctus vitae porta nisl ultrices.</p>
                    </div>
                    <div class="right">
            			<h3>Services</h3>
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ut risus in eros consectetur feugiat. Fusce elementum convallis porttitor. Nulla facilisi. Morbi at erat felis. Aenean ante nisl, pulvinar nec varius ut, egestas sit amet dolor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam vitae fermentum odio. Quisque quis lacus id dolor rhoncus placerat ut eu odio. Duis nec ipsum est. Proin congue faucibus risus, at blandit libero rutrum at. Vivamus ac laoreet lectus. Nam et nisi est. Aliquam hendrerit tristique lectus sit amet fringilla. Aenean pretium lacus vitae massa bibendum feugiat. Suspendisse a mi mauris. Etiam fermentum lacus nec lectus luctus vitae porta nisl ultrices.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="footer">
    <div align="center">
        <div class="innerFooter">
            <a href="http://twitter.com/grails"><div class="twitter"></div></a>
            <a href="http://www.springsource.com"><div class="springSource"></div></a>
            <p>&copy; Copyright 2009 SpringSource.<br/>All Rights Reserved.</p>
        </div>
    </div>
</div>

</body>
</html>