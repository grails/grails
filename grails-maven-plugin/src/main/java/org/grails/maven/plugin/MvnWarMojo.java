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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;

import java.io.File;

/**
 * Creates a WAR archive for the project and puts it in the usual Maven
 * place.
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
    protected File warFile;

    /**
     * Executes the MvnWarMojo on the current project.
     *
     * @throws MojoExecutionException if an error occured while building the webapp
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        runGrails("War");
        String warFileName = project.getArtifactId() + "-" + project.getVersion() + ".war";
        File warGeneratedByGrails = new File(getBasedir(), warFileName);

        warFile = new File(project.getBuild().getDirectory(), warFileName);
        warFile.delete();
        if (!warGeneratedByGrails.renameTo(warFile)) {
            throw new MojoExecutionException("Unable to copy the WAR file to the target directory");
        } else {
            getLog().info("Moved WAR file to '" + warFile + "'.");
        }
    }
}
