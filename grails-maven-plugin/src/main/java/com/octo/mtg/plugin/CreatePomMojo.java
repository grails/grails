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
import com.octo.mtg.plugin.tools.GrailsServices;
import com.octo.mtg.plugin.tools.PomServices;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;

/**
 * Creates a creates a maven 2 POM for on an existing Grails project.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @description Creates a creates a maven 2 POM for on an existing Grails
 * project.
 * @goal create-pom
 * @requiresProject false
 * @since 0.1
 */
public class CreatePomMojo extends AbstractMojo {

    /**
     * The Group Id of the project to be build.
     *
     * @parameter expression="${groupId}"
     * @required
     */
    private String groupId;

    /**
     * Does the plugin generate maven-eclipse-plugin settings in your pom ?
     *
     * @parameter expression="${addEclipseSettings}"
     */
    private boolean addEclipseSettings;

    /**
     * @parameter expression="${plugin.artifactId}"
     * @required
     * @readonly
     */
    private String pluginArtifactId;

    /**
     * @parameter expression="${plugin.groupId}"
     * @required
     * @readonly
     */
    private String pluginGroupId;

    /**
     * @parameter expression="${plugin.version}"
     * @required
     * @readonly
     */
    private String pluginVersion;

    /**
     * @parameter expression="${basedir}"
     * @required
     * @readonly
     */
    private File basedir;

    /**
     * @component
     * @readonly
     */
    protected PomServices pomServices;

    /**
     * @component
     * @readonly
     */
    protected GrailsServices grailsServices;

    public void execute() throws MojoExecutionException, MojoFailureException {
        grailsServices.setBasedir(basedir);
        pomServices.setBasedir(basedir);

        GrailsProject grailsDescr;

        grailsDescr = grailsServices.readProjectDescriptor();

        pomServices.write(grailsServices.createPOM(groupId, grailsDescr, pluginGroupId, pluginArtifactId,
            pluginVersion, addEclipseSettings));
    }
}
