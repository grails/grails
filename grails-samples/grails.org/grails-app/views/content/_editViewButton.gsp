<span id="${id}Button">
    <g:remoteLink onComplete="\$('viewLinks').style.display='none';${ displayEditLinks ? '$(\'editLinks\').style.display=\'inline\';' : ''};${ onComplete ?: ''}" action="${id}WikiPage" id="${content?.title}" update="editPane"><img border="0" src="${createLinkTo(dir:'images/','icon-'+id+'.png')}" width="15" height="15" alt="Icon ${id}" class="inlineIcon" border="0" /></g:remoteLink>
    <g:remoteLink onComplete="\$('viewLinks').style.display='none';${ displayEditLinks ? '$(\'editLinks\').style.display=\'inline\';' : ''};${ onComplete ?: ''}" action="${id}WikiPage" id="${content?.title}" update="editPane">${text}</g:remoteLink>
</span>
