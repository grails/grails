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

import com.octo.mtg.plugin.tools.GrailsServices;
import com.octo.mtg.plugin.tools.MojoServices;
import com.octo.mtg.plugin.tools.PomServices;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.artifact.MavenMetadataSource;
import org.codehaus.groovy.grails.cli.support.GrailsRootLoader;
import org.codehaus.groovy.grails.cli.support.GrailsBuildHelper;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

/**
 * Common services for all Mojos using Grails
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 */
public abstract class AbstractGrailsMojo extends AbstractMojo {
    /**
     * The directory where is launched the mvn command.
     *
     * @parameter default-value="${basedir}"
     * @required
     */
    protected File basedir;

    /**
     * The Grails environment to use.
     *
     * @parameter expression="${grails.env}"
     */
    protected String env;

    /**
     * The path to GRAILS' HOME.
     *
     * @parameter expression="${grailsHome}"
//     * @required
     */
    protected String grailsHome;

    /**
     * POM
     *
     * @parameter expression="${project}"
     * @readonly
     * @required
     */
    protected MavenProject project;

    /**
     * @component
     */
    private ArtifactResolver artifactResolver;

    /**
     * @component
     */
    private ArtifactFactory artifactFactory;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    private ArtifactRepository localRepository;

    /**
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @required
     * @readonly
     */
    private List remoteRepositories;

    /**
     * @component
     */
    private MavenProjectBuilder projectBuilder;

    /**
     * @component
     * @readonly
     */
    private GrailsServices grailsServices;

    /**
     * @component
     * @readonly
     */
    private MojoServices mojoServices;

    /**
     * @component
     * @readonly
     */
    private PomServices pomServices;

    protected File getBasedir() {
        if(basedir == null) {
            throw new RuntimeException("Your subclass have a field called 'basedir'. Remove it and use getBasedir() " +
                "instead.");
        }

        return this.basedir;
    }

    /**
     * OutputStream to write the content of stdout.
     */
    private OutputStream infoOutputStream = new OutputStream() {
        StringBuffer buffer = new StringBuffer();

        public void write(int b) throws IOException {
            if (b == '\n') {
                getLog().info(buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append((char) b);
            }
        }
    };

    /**
     * OutputStream to write the content of stderr.
     */
    private OutputStream warnOutputStream = new OutputStream() {
        StringBuffer buffer = new StringBuffer();

        public void write(int b) throws IOException {
            if (b == '\n') {
                getLog().warn(buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append((char) b);
            }
        }
    };

    protected GrailsServices getGrailsServices() throws MojoExecutionException {
        grailsServices.setBasedir(basedir);
        grailsServices.setDependencies(getPluginDependencies(getBootStrapPOM()));

        return grailsServices;
    }

    protected PomServices getPomServices() {
        pomServices.setBasedir(basedir);

        return pomServices;
    }

    protected Artifact getBootStrapPOM() {
        return this.artifactFactory.createBuildArtifact("org.grails", "grails-bootstrap", "1.1-beta1", "pom");
    }

    protected Artifact getScriptsPOM() {
        return this.artifactFactory.createBuildArtifact("org.grails", "grails-scripts", "1.1-beta1", "pom");
    }

    protected List getPluginDependencies(Artifact pom) throws MojoExecutionException {
        try {
            MavenProject project = this.projectBuilder.buildFromRepository(pom,
                                                                           this.remoteRepositories,
                                                                           this.localRepository);

            //get all of the dependencies for the executable project
            List dependencies = project.getDependencies();

            //make Artifacts of all the dependencies
            Set dependencyArtifacts =
                MavenMetadataSource.createArtifacts(this.artifactFactory, dependencies, null, null, null);

            //not forgetting the Artifact of the project itself
            dependencyArtifacts.add(project.getArtifact());

            //resolve all dependencies transitively to obtain a comprehensive list of assemblies
            List artifacts = new ArrayList(dependencyArtifacts.size());
            for (Iterator iter = dependencyArtifacts.iterator(); iter.hasNext();) {
                Artifact artifact = (Artifact) iter.next();
                this.artifactResolver.resolve(artifact, this.remoteRepositories, this.localRepository);
                artifacts.add(artifact);
            }

            return artifacts;
        } catch ( Exception ex ) {
            throw new MojoExecutionException("Encountered problems resolving dependencies of the executable " +
                                             "in preparation for its execution.", ex);
        }
    }

    protected void runGrails(String targetName) throws MojoExecutionException {
        runGrails(targetName, null, "runtime");
    }

    protected void runGrails(String targetName, String args, String scope) throws MojoExecutionException {
        List pluginDependencies = getPluginDependencies(getBootStrapPOM());
        pluginDependencies.addAll(getPluginDependencies(getScriptsPOM()));
        Set allArtifacts = new HashSet(pluginDependencies);

        URL[] classpath;
        try {
            classpath = new URL[allArtifacts.size() + 1];
            int index = 0;
            for (Iterator iter = allArtifacts.iterator(); iter.hasNext();) {
                classpath[index++] = ((Artifact) iter.next()).getFile().toURI().toURL();
            }

            // Add the "tools.jar" to the classpath so that the Grails
            // scripts can run native2ascii. First assume that "java.home"
            // points to a JRE within a JDK.
            String javaHome = System.getProperty("java.home");
            File toolsJar = new File(javaHome, "../lib/tools.jar");
            if (!toolsJar.exists()) {
                // The "tools.jar" cannot be found with that path, so
                // now try with the assumption that "java.home" points
                // to a JDK.
                toolsJar = new File(javaHome, "tools.jar");
            }
            classpath[classpath.length - 1] = toolsJar.toURI().toURL();

            GrailsRootLoader rootLoader = new GrailsRootLoader(classpath, getClass().getClassLoader());
            GrailsBuildHelper helper = new GrailsBuildHelper(rootLoader);
            configureBuildSettings(helper);

//            mainClass.getDeclaredMethod("setOut", new Class[]{ PrintStream.class }).invoke(
//                    scriptRunner,
//                    new Object[] { new PrintStream(infoOutputStream) });

            int retval = helper.execute(targetName, args, env);
            if (retval != 0) {
                throw new MojoExecutionException("Grails returned non-zero value.");
            }
        } catch (MojoExecutionException ex) {
            // Simply rethrow it.
            throw ex;
        } catch (Exception ex) {
            throw new MojoExecutionException("Unable to start Grails", ex);
        }
    }

    private void configureBuildSettings(GrailsBuildHelper helper)
            throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, MojoExecutionException, NoSuchMethodException, InvocationTargetException {
        String targetDir = this.project.getBuild().getDirectory();
        helper.setCompileDependencies(artifactsToFiles(this.project.getCompileArtifacts()));
        helper.setTestDependencies(artifactsToFiles(this.project.getTestArtifacts()));
        helper.setRuntimeDependencies(artifactsToFiles(this.project.getRuntimeArtifacts()));
        helper.setProjectWorkDir(new File(targetDir));
        helper.setClassesDir(new File(targetDir, "classes"));
        helper.setTestClassesDir(new File(targetDir, "test-classes"));
        helper.setResourcesDir(new File(targetDir, "resources"));
        helper.setProjectPluginsDir(new File(this.project.getBasedir(), "plugins"));
    }

    private List artifactsToFiles(Collection artifacts) {
        List files = new ArrayList(artifacts.size());
        for (Iterator iter = artifacts.iterator(); iter.hasNext();) {
            files.add(((Artifact) iter.next()).getFile());
        }

        return files;
    }
}
