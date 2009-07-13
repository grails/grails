/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.maven.plugin;

import org.grails.maven.plugin.tools.GrailsPluginProject;
import org.grails.maven.plugin.tools.GrailsProject;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Validate consistency between Grails and Maven settings.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @description Validate consistency between Grails (application.properties) and Maven (pom.xml) settings.
 * @goal validate
 * @phase validate
 * @requiresDependencyResolution runtime
 * @since 0.1
 */
public class MvnValidateMojo extends AbstractGrailsMojo {

    /**
     * The artifact id of the project.
     *
     * @parameter expression="${project.artifactId}"
     * @required
     * @readonly
     */
    private String artifactId;

    /**
     * The version id of the project.
     *
     * @parameter expression="${project.version}"
     * @required
     * @readonly
     */
    private String version;

    public void execute() throws MojoExecutionException, MojoFailureException {
        GrailsProject grailsProject;
        try {
            grailsProject = getGrailsServices().readProjectDescriptor();
        } catch (MojoExecutionException e) {
            getLog().info("No Grails application found - skipping validation.");
            return;
        }
        
        if (!artifactId.equals(grailsProject.getAppName())) {
            throw new MojoFailureException("app.name [" + grailsProject.getAppName() + "] in " +
                "application.properties is different of the artifactId [" + artifactId + "] in the pom.xml");
        }

        // We have to set the application version in grails settings for old versions
        if (grailsProject.getAppVersion() == null) {
            grailsProject.setAppVersion(GrailsProject.DEFAULT_APP_VERSION);
            getLog().warn("application.properties didn't contain an app.version property");
            getLog().warn("Setting to default value '" + grailsProject.getAppVersion() + "'.");

            getGrailsServices().writeProjectDescriptor(getBasedir(), grailsProject);
        }
        
        String pomVersion = version.trim();
        String grailsVersion = grailsProject.getAppVersion().trim();

        if (!grailsVersion.equals(pomVersion)) {
            throw new MojoFailureException("app.version [" + grailsVersion + "] in " +
                "application.properties is different of the version [" + pomVersion + "] in the pom.xml");
        }

    }
}
