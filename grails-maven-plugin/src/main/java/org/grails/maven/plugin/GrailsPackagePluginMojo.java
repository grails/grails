/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.maven.plugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;

/**
 * Packages the Grails plugin.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @description Packages the Grails plugin.
 * @goal package-plugin
 * @requiresProject true
 * @requiresDependencyResolution runtime
 * @since 0.4
 */
public class GrailsPackagePluginMojo extends AbstractGrailsMojo {

    /**
     * The artifact that this project produces.
     *
     * @parameter expression="${project.artifact}"
     * @required
     * @readonly
     */
    private Artifact artifact;

    /**
     * The artifact handler.
     *
     * @parameter expression="${component.org.apache.maven.artifact.handler.ArtifactHandler#grails-plugin}"
     * @required
     * @readonly
     */
    protected ArtifactHandler artifactHandler;

    public void execute() throws MojoExecutionException, MojoFailureException {
        // First package the plugin using the Grails script.
        runGrails("PackagePlugin");

        // Now move the ZIP from the project directory to the build
        // output directory.
        String zipFileName = project.getArtifactId() + "-" + project.getVersion() + ".zip";
        File zipGeneratedByGrails = new File(getBasedir(), zipFileName);

        File mavenZipFile = new File(project.getBuild().getDirectory(), zipFileName);
        mavenZipFile.delete();
        if (!zipGeneratedByGrails.renameTo(mavenZipFile)) {
            throw new MojoExecutionException("Unable to copy the plugin ZIP to the target directory");
        } else {
            getLog().info("Moved plugin ZIP to '" + mavenZipFile + "'.");
        }

        // Attach the zip file to the "grails-plugin" artifact, otherwise
        // the "install" and "deploy" phases won't work.
        artifact.setFile(mavenZipFile);
        artifact.setArtifactHandler(artifactHandler);
    }
}
