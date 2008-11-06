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
package com.octo.mtg.plugin;

import com.octo.mtg.plugin.tools.GrailsProject;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;

/**
 * Creates a WAR archive and register it in maven.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @description Creates a WAR archive and register it in maven.
 * @goal maven-war
 * @phase package
 * @requiresDependencyResolution
 * @since 0.1
 */
public class MvnWarMojo extends AbstractGrailsMojo {

    /**
     * The maven artifact.
     *
     * @parameter expression="${project.artifact}"
     * @required
     * @readonly
     */
    private Artifact artifact;

    /**
     * The build directory. The WAR file will end up here.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     * @readonly
     */
    private File buildDirectory;

    /**
     * The final name of the artifact without the extension.
     *
     * @parameter expression="${project.build.finalName}"
     * @required
     * @readonly
     */
    private String finalName;

    /**
     * The artifact handler.
     *
     * @parameter expression="${component.org.apache.maven.artifact.handler.ArtifactHandler#grails-app}"
     * @required
     * @readonly
     */
    protected ArtifactHandler artifactHandler;

    /**
     * Executes the MvnWarMojo on the current project.
     *
     * @throws MojoExecutionException if an error occured while building the webapp
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        runGrails("War");
//        GrailsProject grailsProject = getGrailsServices().readProjectDescriptor();
//
//        getGrailsServices().launchGrails(grailsHome, env, "war");
//        String warFileName = grailsProject.getAppName() + "-" + grailsProject.getAppVersion() + ".war";
//        File warGeneratedByGrails = new File(getBasedir(), warFileName);
//
//        if (!buildDirectory.isDirectory() && !buildDirectory.mkdirs()) {
//            throw new MojoExecutionException("Unable to create directory: " + buildDirectory.getAbsolutePath());
//        }
//
//        File mavenWarFile = new File(buildDirectory, finalName + ".war");
//        mavenWarFile.delete();
//        if (!warGeneratedByGrails.renameTo(mavenWarFile)) {
//            throw new MojoExecutionException("Unable to copy the war in the target directory");
//        }
//        artifact.setFile(mavenWarFile);
//        artifact.setArtifactHandler(artifactHandler);
    }
}
