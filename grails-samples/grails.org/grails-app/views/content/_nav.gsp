
<div id="search">
    <g:form method="GET" url="[controller:'searchable']" name="searchForm">
        <input type="text" accessKey="s" name="q" /></g:form>
</div><!-- / search -->

<div id="nav">
            <ul>
                <li><g:link controller="content" id="Installation">Getting Started</g:link>
                <ul>
                    <li><g:link controller="content" id="Installation">Installation</g:link></li>
                    <li><g:link controller="content" id="Quick Start">Quick Start</g:link></li>
                    <li><g:link controller="content" id="IDE Integration">IDE Setup</g:link></li>
                    <li><g:link controller="content" id="Tutorials">Tutorials</g:link></li>
                    <li><g:link controller="content" id="Grails Screencasts">Screencasts</g:link></li>
                </ul>
                </li>
                <li><g:link controller="content" id="Reference">Reference</g:link>
                <ul>
                    <li><g:link controller="content" id="Documentation">Documentation</g:link></li>
                    <li><g:link controller="content" id="FAQ">FAQs</g:link></li>
                    <li><g:link controller="content" id="Roadmap">Roadmap</g:link></li>
                </ul>
                </li>
                <li><g:link controller="content" id="Community">Community</g:link>
                <ul>
                    <plugin:isAvailable name="jobs">
                        <li><g:link controller="job" action="list">Job Listings</g:link></li>    
                    </plugin:isAvailable>
                    <li><g:link controller="content" id="Testimonials">Testimonials</g:link></li>
                    <li><g:link controller="content" id="Community">Contributing</g:link></li>
                    <li><g:link controller="content" id="Plugins">Plugins</g:link></li>
                    <li><g:link controller="content" id="Mailing lists">Mailing Lists</g:link></li>
                </ul>
                </li>
    <!--			<li><a href="">Weblog</a></li>-->
            </ul>
</div><!-- / nav -->