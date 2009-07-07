<div id="contentArea">
    <div id="pluginMenu">
        <h1>Plugins</h1>
        <div class="description">
            <p/>Welcome to the Grails plugin portal. The place where you can find information about the latest plugins available for the Grails framework.
        </div>
        <ul id="pluginSorters">
            <li class="all"><g:link action="home" params="[category:'all']">All</g:link></li>
            <li class="featured"><g:link action="home" params="[category:'featured']">Featured</g:link></li>
            <li class="popular"><g:link action="home" params="[category:'popular']">Most Popular</g:link></li>
            <li class="recentlyUpdated"><g:link action="home" params="[category:'recentlyUpdated']">Recently Updated</g:link></li>
            <li class="newest"><g:link action="home" params="[category:'newest']">Newest</g:link></li>
        </ul>
        <div class="createPlugin">
            <h3>Want to create a plugin?</h3>
            <div class="detail">
                If you are interested in creating and distributing a plugin in the Grails central repository, take a look at this <a href="http://grails.org/doc/latest/guide/12. Plug-ins.html">user guide section</a>.
            </div>
        </div>
        <div class="bottom"></div>
        <div class="links">
            <h3>Helpful Links</h3>
            <ul>
                <li><a href="http://grails.org/Creating+Plugins">Creating a plugin</a></li>
                <li><a href="http://svn.grails-plugins.codehaus.org">Plugins Fisheye</a></li>
                <li><a href="http://jira.codehaus.org/browse/GRAILSPLUGINS">Plugins JIRA</a></li>
            </ul>
        </div>
    </div>

    <div id="content">
        <div id="searchBar">
            <g:render template="searchBar"/>
        </div>
		<g:layoutBody />
    </div>
</div><!-- contentArea -->

<div id="btmSectionGraphicsWrapper">
    <div id="mountainLeft"></div>
    <div id="knight"></div>
    <div id="mountainRight"></div>
    <div id="castle"></div>
</div><!-- btmSectionGraphicsWrapper-->
