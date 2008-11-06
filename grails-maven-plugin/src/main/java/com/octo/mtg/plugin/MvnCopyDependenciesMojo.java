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
package com.octo.mtg.plugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Copy the POM dependencies in the lib directory of the grails project.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @description Copy the POM dependencies in the lib directory of the grails project.
 * @goal copy-dependencies
 * @phase process-resources
 * @requiresDependencyResolution runtime
 * @since 0.3
 */
public class MvnCopyDependenciesMojo extends AbstractGrailsMojo {
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Executing copy dependencies...");
        File outputDir = new File(project.getBuild().getDirectory(), "grails-lib");

//        if (!outputDir.exists()) {
//            getLog().info("Creating '" + outputDir + "' directory for Grails JARs");
//            outputDir.mkdir();
//        }

        for (Iterator it = this.project.getRuntimeArtifacts().iterator(); it.hasNext();) {
            Artifact artifact = (Artifact) it.next();

            if ("grails-plugin".equals(artifact.getType())) {
                installGrailsPlugin(artifact);
            } else {
                installJarArtifact(artifact, outputDir);
            }
        }
    }

    private void installGrailsPlugin(Artifact artifact) throws MojoExecutionException {
        File source = artifact.getFile();

        File target = new File(new File(getBasedir(), "plugins"), source.getName());

        if (target.exists() && target.lastModified() > source.lastModified()) {
            getLog().debug("Target is newer than source, skipping " + artifact.getId() + ".");

            return;
        }

        // This is a workaround the grails binary that doesn't parse command line arguments with spaces.
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File tmp = new File(tmpDir, source.getName());

        getLog().debug("Copying the .zip file to a temporary file: '" + tmp.getAbsolutePath() + "'.");

        try {
            FileUtils.copyFileToDirectory(source, tmpDir);
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to copy file from '" + source.getAbsolutePath() + "' to " +
                "directory '" + tmpDir.getAbsolutePath() + "'.", e);
        }

        getLog().debug("Installing plugin: '" + source.getAbsolutePath() + "'.");

        getGrailsServices().launchGrails(grailsHome, env, "install-plugin", new String[]{tmp.getAbsolutePath()});
    }

    private void installJarArtifact(Artifact artifact, File outputDir) throws MojoExecutionException {
        File source = artifact.getFile();

        File target = new File(outputDir, source.getName());

        if (target.exists() && target.lastModified() > source.lastModified()) {
            getLog().debug("Target is newer than source, skipping " + artifact.getId() + ".");

            return;
        }

        try {
            getLog().debug("Copying file from '" + source.getAbsolutePath() + "' to '" +
                target.getAbsolutePath() + "'.");
            FileUtils.copyFile(source, target);
        } catch (IOException e) {
            throw new MojoExecutionException("Could not copy file from '" + source.getAbsolutePath() + "' to '" +
                target.getAbsolutePath() + "'.", e);
        }
    }
}
