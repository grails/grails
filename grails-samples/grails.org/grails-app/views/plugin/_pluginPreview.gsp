<div class="currentPlugin">
	<div class="header">
		<div class="ratings">
			<rateable:ratings id="rating${plugin.id}" bean="${plugin}" active="false"/>
		</div>						
    	<h4>${plugin.title}</h4>						
        <g:if test="${plugin.official}">
            <div class="supported">supported by SpringSource</div>
        </g:if>
        <div class="detail">
            <div><span class="label">Tags:</span> ${plugin.tags.join(', ')}</div>								
			<!-- <g:if test="${plugin.tags}"> -->

			<!-- </g:if> -->

            <div><span class="label">Grails Version:</span> ${plugin.grailsVersion}</div>
            <div><span class="label">Current Release:</span> ${plugin.currentRelease}</div>
        </div>

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

    </div>
    <div class="bottom">
		<div class="moreInfo">
            <g:link action="show" params="${[name:plugin.name]}">
				<img alt="More Info" src="${resource(dir:'images/new/plugins/Buttons', file:'more_info_btn.png')}" border="0" /> 
			</g:link>
		
		</div>
        <div class="comments">
			<g:link fragment="comments" action="show" params="${[name:plugin.name]}">${plugin.comments.size()} Comments 
				<img src="${resource(dir:'images/new/plugins/icons', file:'comments.png')}" border="0" />
			</g:link>
		</div>
		<div class="download">
        	<a href="${plugin.downloadUrl}">Download</a>
		</div>
    </div>
</div>
