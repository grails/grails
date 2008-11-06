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
import org.codehaus.groovy.tools.RootLoader;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
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
        return this.artifactFactory.createBuildArtifact("org.grails", "grails-bootstrap", "1.1-SNAPSHOT", "pom");
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
//        List pluginDependencies = getPluginDependencies(getBootStrapPOM());
//        List runtimeDependencies = this.project.getRuntimeArtifacts();
//        List systemDependencies = this.project.getSystemArtifacts();
        Set allArtifacts = new HashSet(getPluginDependencies(getBootStrapPOM()));
        allArtifacts.addAll(this.project.getDependencyArtifacts());

        if (scope.equals("compile")) {
            allArtifacts.addAll(this.project.getCompileArtifacts());
        } else if (scope.equals("runtime")) {
            allArtifacts.addAll(this.project.getRuntimeArtifacts());
        } else if (scope.equals("test")) {
            allArtifacts.addAll(this.project.getTestArtifacts());
        } else if (scope.equals("system")) {
            allArtifacts.addAll(this.project.getSystemArtifacts());
        }

        URL[] classpath;
        try {
            classpath = new URL[allArtifacts.size()];
            int index = 0;
            for (Iterator iter = allArtifacts.iterator(); iter.hasNext();) {
                classpath[index++] = ((Artifact) iter.next()).getFile().toURI().toURL();
            }

//            for (Iterator iter = runtimeDependencies.iterator(); iter.hasNext();) {
//                classpath[index++] = ((Artifact) iter.next()).getFile().toURI().toURL();
//            }
//
//            for (Iterator iter = systemDependencies.iterator(); iter.hasNext();) {
//                classpath[index++] = ((Artifact) iter.next()).getFile().toURI().toURL();
//            }

//            Commandline cmd = new Commandline();
//            cmd.setWorkingDirectory(getBasedir());
//            cmd.setExecutable("java");
//            cmd.createArg().setValue("-cp");
//            cmd.createArg().setValue(toClasspath(classpath));
//            cmd.createArg().setValue("org.codehaus.groovy.grails.cli.GrailsScriptRunner");
//            cmd.createArg().setValue(targetName);
//
//            int returnCode = mojoServices.executeCommandLine(cmd, System.in, infoOutputStream, warnOutputStream);
//            getLog().debug("Grails ended with the return code: " + returnCode);
//            if (returnCode != 0) {
//                throw new MojoExecutionException("Grails ended with a non null return code: " + returnCode);
//            }

            // Setup grails env
//            if (env != null) {
//                // For default environments, we use the command line arg
//                // as a workaround for [GRAILS-1658]
//                if ("dev".equals(env)) {
//                    cmd.createArg().setValue("dev");
//                } else if ("prod".equals(env)) {
//                    cmd.createArg().setValue("prod");
//                } else if ("test".equals(env)) {
//                    cmd.createArg().setValue("test");
//                } else {
//                    cmd.createArg().setValue("-Dgrails.env=" + env);
//                }
//            }
            List mainArgs = new ArrayList();
            mainArgs.add(targetName);

//            if (env != null) {
//                mainArgs.add(env);
//            }

//            if (args != null && args.length > 0) {
//                mainArgs.addAll(Arrays.asList(args));
//            }

            System.setProperty("grails.project.work.dir", this.project.getBuild().getDirectory());

            RootLoader rootLoader = new RootLoader(classpath, getClass().getClassLoader());
            Class mainClass = rootLoader.loadClass("org.codehaus.groovy.grails.cli.GrailsScriptRunner");
            Object scriptRunner = mainClass.newInstance();

            mainClass.getDeclaredMethod("setOut", new Class[]{ PrintStream.class }).invoke(
                    scriptRunner,
                    new Object[] { new PrintStream(infoOutputStream) });

            Method mainMethod = mainClass.getDeclaredMethod(
                    "executeCommand",
                    new Class[]{ String.class, String.class, String.class });
            Object retval = mainMethod.invoke(
                    scriptRunner,
                    new Object[] { targetName, args, env });
            if (((Integer) retval).intValue() != 0) {
                throw new MojoExecutionException("Grails returned non-zero value.");
            }
//        } catch (MalformedURLException ex) {
//            throw new MojoExecutionException("Something went wrong", ex);
//        } catch (CommandLineException ex) {
//            throw new MojoExecutionException("Something went wrong", ex);
        } catch (MojoExecutionException ex) {
            // Simply rethrow it.
            throw ex;
        } catch (Exception ex) {
            throw new MojoExecutionException("Unable to start Grails", ex);
        }
    }

    private String toClasspath(URL[] urls) {
        if (urls.length == 0) return "";

        String pathSeparator = System.getProperty("path.separator");
        StringBuffer buf = new StringBuffer(urls[0].getPath());
        for (int i = 1; i < urls.length; i++) {
            buf.append(pathSeparator);
            buf.append(urls[i].getPath());
        }

        return buf.toString();
    }
}
