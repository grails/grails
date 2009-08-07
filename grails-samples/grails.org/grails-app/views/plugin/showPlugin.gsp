<%@ page import="org.grails.plugin.Plugin" %>
<g:applyLayout name="pluginDetails">
	<div id="pluginDetailsBox" align="center">
		
		<div id="pluginDetailsContainer">
			
			<gui:dialog id='loginDialog' title="Login required" modal="true">
			    <div id='loginFormDiv'></div>
			</gui:dialog>

			<div id="downloadBox">
				<a href="${plugin.downloadUrl}"><img src="${resource(dir:'images/new/plugins/Buttons', file:'downloadBox_btn.png')}" alt="Download" border="0"></a>
			</div>

			<h1 id="pluginBoxTitle">${plugin?.title}</h1>

			<div class="ratingBox">
				<rateable:ratings bean="${plugin}"/>						
			</div>


		    <div class="pluginDetail">
		        <table>
		            <tr>
		                <th>Author(s):</th>
		                <td>${plugin.author}</td>
		            </tr>
		            <tr>
		                <th>Current Release:</th>
		                <td>${plugin.currentRelease}</td>
		            </tr>
		            <tr>
		                <th>Grails Version:</th>
		                <td>${plugin.grailsVersion ?: '?'}</td>
		            </tr>
		            <tr>
		                <th>Tags</th>
		                <td class='tags'>
		                    <span id='pluginTags'>
		                        <g:render template='tags' var='plugin' bean="${plugin}"/>
		                    </span>
		                    <span id='addTagTrigger'><img src="${createLinkTo(dir: 'images/famfamfam', file: 'add.png')}"/></span>
		                </td>
		            </tr>
		        </table>
		    </div>
		    <div class="links">
		        <a href="${plugin.fisheye}">
					<div class="fisheye">
						<img src="${resource(dir:'images/new/plugins/icons', file:'fisheye.png')}" border="0" /> 
						Fisheye
					</div>
				</a>
		        <a href="${plugin.documentationUrl}">
					<div class="docs">
						<img src="${resource(dir:'images/new/plugins/icons', file:'doc.png')}" border="0" /> 
						Docs
					</div>

		        </a>
		        <g:link controller="plugin" action="editPlugin" id="${plugin.id}">
					<div class="edit">
						<img src="${resource(dir:'images/new/plugins/icons/16x16_icons', file:'edit.png')}" border="0" /> 
						Edit Plugin
					</div>
				</g:link>
		    </div>
			
		</div>
		
	</div>

	
</div>



    %{--
        Logged in users will be able to add tags
    --}%
    <jsec:isLoggedIn>
        <gui:dialog id='addTagDialog'
            title='Add Tags'
            form='true' controller='plugin' action='addTag' params="${[id:plugin.id]}"
            triggers="[show:[id:'addTagTrigger',on:'click']]"
            update='pluginTags'
        >
            <gui:autoComplete id='newTag'
                controller='tag' action='autoCompleteNames'
                resultName='tagResults'
                labelField='name'
                minQueryLength='1'
                queryDelay='1'
            />
        </gui:dialog>

        <script>
            YAHOO.util.Event.onDOMReady(function() {
                // on show, put the dialog in the right place
                GRAILSUI.addTagDialog.subscribe('show', function() {
                    var pos = YAHOO.util.Dom.getXY('addTagTrigger');
                    this.cfg.setProperty('x',pos[0]+20);
                    this.cfg.setProperty('y',pos[1]+20);
                });
                // on hide, clear out the text within it
                GRAILSUI.addTagDialog.subscribe('hide', function() {
                    document.getElementById('newTag').value = ''
                });
            });
        </script>
    </jsec:isLoggedIn>
    %{-- Unauthenticated users get defered to the login screen --}%
    <jsec:isNotLoggedIn>
        <script>
            YAHOO.util.Event.onDOMReady(function() {
                // on show, put the dialog in the right place
                YAHOO.util.Event.on('addTagTrigger', 'click', function() {
                    window.location = "${createLink(controller:'user', action:'login', params:[originalURI:request.forwardURI])}";
                });
                // also hang up rating click if not logged in, redirect to login page with originalURI of this page
                // for redirect
                YAHOO.util.Event.on('ratingdiv', 'click', function(e) {
                    YAHOO.util.Event.stopEvent(e);
                    window.location = "${createLink(controller:'user', action:'login', params:[originalURI:request.forwardURI])}";
                });
            });
        </script>
    </jsec:isNotLoggedIn>

	<div id="pluginContent" align="center">
	    <cache:text key="pluginTabs_${plugin.id}">
	        <gui:tabView>
	            <g:each var="wiki" in="${Plugin.WIKIS}">
	                <gui:tab id="${wiki}Tab" label="${wiki[0].toUpperCase() + wiki[1..-1]}" active="${wiki == 'description'}">
	                    <g:render template="../content/viewActions" model="${[content: plugin[wiki], update: wiki + 'Tab', editFormName: wiki + 'EditForm']}"/>
	                    <div class='${wiki}, wikiPage'><wiki:text>${plugin[wiki]?.body}</wiki:text></div>


	                </gui:tab>
	            </g:each>
	        </gui:tabView>
		</cache:text>		
	</div>


</g:applyLayout>