<cache:text id="downloadPanel">
    <div id="downloadLink">
        <download:latest var="grailsDownload" software="Grails" />
        <download:latest var="grailsDocs" software="Grails Documentation" />
        <download:link software="Grails" version="${grailsDownload?.softwareVersion}" file="Binary Zip"><img border="0" src="images/download-icon.png" width="60" height="61" alt="Download Icon" style="float: left; margin: 3px 10px 25px 0px;" /></download:link>
        <h3><a href="Download">Download Grails ${grailsDownload?.softwareVersion}</a></h3>
        Download zip: <download:link software="Grails" version="${grailsDownload?.softwareVersion}" file="Binary Zip">Binary</download:link> | <download:link software="Grails" version="${grailsDownload?.softwareVersion}" file="Source Zip">Source</download:link> <br />
        Download tar/gz: <download:link software="Grails" version="${grailsDownload?.softwareVersion}" file="Binary Tar/GZ">Binary</download:link> | <download:link software="Grails" version="${grailsDownload?.softwareVersion}" file="Source Tar/GZ">Source</download:link> <br />
        Release Notes: <a href="http://grails.org/${grailsDownload?.softwareVersion}+Release+Notes">HTML</a><br />
        User Documentation: <a href="http://grails.org/doc/1.0.x/">Online</a> | <download:link software="Grails Documentation" version="${grailsDocs?.softwareVersion}" file="Binary Zip">Zip</download:link> <br />

    </div><!-- / downloadLink -->
</cache:text>
