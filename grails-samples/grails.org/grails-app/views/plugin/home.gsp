<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <feed:meta kind="atom" version="1.0" controller="plugin" action="feed"/>
    <rateable:resources />
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'ratings.css')}"/>

    <title>Grails Plugins</title>
    <meta content="pluginLayout" name="layout"/>
</head>
<body>

<g:applyLayout name="pluginNav">
	<g:if test="${viewType == 'all'}">
		<cache:text id="allPluginList">
			<div id="contentWindowTop">
			
			</div>
			<div id="contentBody">
				<h2>All Plugins (${totalPlugins} Total)</h2>
				<div id="currentPlugins">
					<g:each var="entry" in="${currentPlugins}">
						<div class="pluginGroup">
							
							<h3><a name="${entry.key}">${entry.key}</a></h3>
							<ul>
								<g:each var="plugin" in="${entry.value}">
									<g:if test="${plugin[0]}">
										<li><g:link action="show" params="${[name:plugin[0]]}">${plugin[0]}</g:link> - <wiki:shorten text="${plugin[1]}" length="50" /></li>
									</g:if>									
								</g:each>						
							</ul>
						</div>
					</g:each>
				</div>			
			</div>
			<div id="contentFooter">
			
			</div>
		
		</cache:text>
	</g:if>
	<g:else>
		<div id="currentPlugins">
		    <g:each var="plugin" in="${currentPlugins}">
				<tmpl:pluginPreview plugin="${plugin}" />
		    </g:each>
		</div>
		<div id="paginationPlugins">
			<g:paginate total="${totalPlugins}" params="[category:category]" next="&gt;" prev="&lt;"></g:paginate>
		</div>
	
	</g:else>
</g:applyLayout>



</body>
</html>
