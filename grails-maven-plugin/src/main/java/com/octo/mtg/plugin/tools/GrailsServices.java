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
package com.octo.mtg.plugin.tools;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface GrailsServices {

    /**
     * Sets the basedir that all commands are executed from
     *
     * @since 0.3
     */
    void setBasedir(File basedir);

    /**
     * Create a pom from a grails project.
     *
     * @param newProjectGroupId
     *                The groupId to identify the project
     * @param grailsProjectDescriptor
     *                The Grails project descriptor
     * @param mtgGroupId
     *                The groupId of the plugin
     * @param mtgArtifactId
     *                The artifactId of the plugin
     * @param grailsVersion
     *                The grails version used by the application
     * @return a maven's POM
     * @since 0.1
     */
    MavenProject createPOM(String newProjectGroupId, GrailsProject grailsProjectDescriptor,
                           String mtgGroupId, String mtgArtifactId, String grailsVersion);

    /**
     * Create a pom from a grails project.
     *
     * @param newProjectGroupId
     *                The groupId to identify the project
     * @param grailsProjectDescriptor
     *                The Grails project descriptor
     * @param mtgGroupId
     *                The groupId of the plugin
     * @param mtgArtifactId
     *                The artifactId of the plugin
     * @param grailsVersion
     *                The grails version used by the application
     * @param addEclipseSettings
     *                Activate or not the generation of the entry to configure
     *                the eclipse plugin
     * @return a maven's POM
     * @since 0.3
     */
    MavenProject createPOM(String newProjectGroupId, GrailsProject grailsProjectDescriptor, String mtgGroupId,
                           String mtgArtifactId, String grailsVersion, boolean addEclipseSettings);

    /**
     * Read a grails project descriptor (application.properties) from a file.
     *
     * @return A Grails Project Descriptor
     * @throws Exception
     *                 if a problem occurs
     */
    GrailsProject readProjectDescriptor() throws MojoExecutionException;

    /**
     * Write a grails project descriptor (application.properties) in a file.
     *
     * @param projectDir
     *                The Grails project directory.
     * @param grailsProjectDescriptor
     *                The descriptor to write.
     * @throws FileNotFoundException
     *                 If the project directory isn't found.
     * @throws IOException
     *                 If a problem occurs during the write.
     */
    void writeProjectDescriptor(File projectDir, GrailsProject grailsProjectDescriptor)
        throws MojoExecutionException;

    GrailsPluginProject readGrailsPluginProject() throws MojoExecutionException;
}
