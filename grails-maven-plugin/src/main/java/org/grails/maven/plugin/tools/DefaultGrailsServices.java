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
package org.grails.maven.plugin.tools;

import grails.util.GrailsNameUtils;
import groovy.lang.GroovyClassLoader;

import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @plexus.component role="org.grails.maven.plugin.tools.GrailsServices"
 * @since 0.1
 */
public class DefaultGrailsServices extends AbstractLogEnabled implements GrailsServices {

    private static final String FILE_SUFFIX = "GrailsPlugin.groovy";

    private File _basedir;
    private List _dependencyPaths;

    /**
     * OutputStream to write the content of stdout.
     */
    private OutputStream infoOutputStream = new OutputStream() {
        StringBuffer buffer = new StringBuffer();

        public void write(int b) throws IOException {
            if (b == '\n') {
                getLogger().info(buffer.toString());
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
                getLogger().warn(buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append((char) b);
            }
        }
    };

    private File getBasedir() {
        if (_basedir != null) {
            return _basedir;
        }

        throw new RuntimeException("The basedir has to be set before any of the service methods are invoked.");
    }

    // -----------------------------------------------------------------------
    // GrailsServices Implementation
    // -----------------------------------------------------------------------

    public void setBasedir(File basedir) {
        this._basedir = basedir;
    }

    public MavenProject createPOM(String groupId, GrailsProject grailsProjectDescriptor, String mtgGroupId,
                                  String grailsPluginArtifactId, String mtgVersion) {
        return createPOM(groupId, grailsProjectDescriptor, mtgGroupId, grailsPluginArtifactId, mtgVersion, false);
    }

    public MavenProject createPOM(String groupId, GrailsProject grailsProjectDescriptor, String mtgGroupId,
                                  String grailsPluginArtifactId, String mtgVersion, boolean addEclipseSettings) {
        MavenProject pom = new MavenProject();
        if (pom.getBuild().getPluginManagement() == null) {
            pom.getBuild().setPluginManagement(new PluginManagement());
        }
        PluginManagement pluginMgt = pom.getPluginManagement();

        // Those four properties are needed.
        pom.setModelVersion("4.0.0");
        pom.setPackaging("grails-app");
        // Specific for GRAILS
        pom.getModel().getProperties().setProperty("grailsHome", "${env.GRAILS_HOME}");
        pom.getModel().getProperties().setProperty("grailsVersion", grailsProjectDescriptor.getAppGrailsVersion());
        // Add our own plugin
        Plugin grailsPlugin = new Plugin();
        grailsPlugin.setGroupId(mtgGroupId);
        grailsPlugin.setArtifactId(grailsPluginArtifactId);
        grailsPlugin.setVersion(mtgVersion);
        grailsPlugin.setExtensions(true);
        pom.addPlugin(grailsPlugin);
        // Add compiler plugin settings
        Plugin compilerPlugin = new Plugin();
        compilerPlugin.setGroupId("org.apache.maven.plugins");
        compilerPlugin.setArtifactId("maven-compiler-plugin");
        Xpp3Dom compilerConfig = new Xpp3Dom("configuration");
        Xpp3Dom source = new Xpp3Dom("source");
        source.setValue("1.5");
        compilerConfig.addChild(source);
        Xpp3Dom target = new Xpp3Dom("target");
        target.setValue("1.5");
        compilerConfig.addChild(target);
        compilerPlugin.setConfiguration(compilerConfig);
        pom.addPlugin(compilerPlugin);
        // Add eclipse plugin settings
        if (addEclipseSettings) {
            Plugin warPlugin = new Plugin();
            warPlugin.setGroupId("org.apache.maven.plugins");
            warPlugin.setArtifactId("maven-war-plugin");
            Xpp3Dom warConfig = new Xpp3Dom("configuration");
            Xpp3Dom warSourceDirectory = new Xpp3Dom("warSourceDirectory");
            warSourceDirectory.setValue("web-app");
            warConfig.addChild(warSourceDirectory);
            warPlugin.setConfiguration(warConfig);
            pluginMgt.addPlugin(warPlugin);

            Plugin eclipsePlugin = new Plugin();
            eclipsePlugin.setGroupId("org.apache.maven.plugins");
            eclipsePlugin.setArtifactId("maven-eclipse-plugin");
            Xpp3Dom configuration = new Xpp3Dom("configuration");
            Xpp3Dom projectnatures = new Xpp3Dom("additionalProjectnatures");
            Xpp3Dom projectnature = new Xpp3Dom("projectnature");
            projectnature.setValue("org.codehaus.groovy.eclipse.groovyNature");
            projectnatures.addChild(projectnature);
            configuration.addChild(projectnatures);
            Xpp3Dom additionalBuildcommands = new Xpp3Dom(
                "additionalBuildcommands");
            Xpp3Dom buildcommand = new Xpp3Dom("buildcommand");
            buildcommand.setValue("org.codehaus.groovy.eclipse.groovyBuilder");
            additionalBuildcommands.addChild(buildcommand);
            configuration.addChild(additionalBuildcommands);
            // Xpp3Dom additionalProjectFacets = new Xpp3Dom(
            // "additionalProjectFacets");
            // Xpp3Dom jstWeb = new Xpp3Dom("jst.web");
            // jstWeb.setValue("2.5");
            // additionalProjectFacets.addChild(jstWeb);
            // configuration.addChild(additionalProjectFacets);
            Xpp3Dom packaging = new Xpp3Dom("packaging");
            packaging.setValue("war");
            configuration.addChild(packaging);

            eclipsePlugin.setConfiguration(configuration);
            pluginMgt.addPlugin(eclipsePlugin);
        }
        // Change the default output directory to generate classes
        pom.getModel().getBuild().setOutputDirectory("web-app/WEB-INF/classes");

        pom.setArtifactId(grailsProjectDescriptor.getAppName());
        pom.setName(grailsProjectDescriptor.getAppName());
        pom.setGroupId(groupId);
        pom.setVersion(grailsProjectDescriptor.getAppVersion());
        if (!grailsProjectDescriptor.getAppVersion().endsWith("SNAPSHOT")) {
            getLogger().warn("=====================================================================");
            getLogger().warn("If your project is currently in development, in accordance with maven ");
            getLogger().warn("standards, its version must be " + grailsProjectDescriptor.getAppVersion() + "-SNAPSHOT and not " + grailsProjectDescriptor.getAppVersion() + ".");
            getLogger().warn("Please, change your version in the application.properties descriptor");
            getLogger().warn("and regenerate your pom.");
            getLogger().warn("=====================================================================");
        }
        return pom;
    }

    public GrailsProject readProjectDescriptor() throws MojoExecutionException {
        // Load existing Grails properties
        FileInputStream fis = null;
        try {
            Properties properties = new Properties();
            fis = new FileInputStream(new File(getBasedir(), "application.properties"));
            properties.load(fis);

            GrailsProject grailsProject = new GrailsProject();
            grailsProject.setAppGrailsVersion(properties.getProperty("app.grails.version"));
            grailsProject.setAppName(properties.getProperty("app.name"));
            grailsProject.setAppVersion(properties.getProperty("app.version"));

            return grailsProject;
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to read grails project descriptor.", e);
        } finally {
            IOUtil.close(fis);
        }
    }

    public void writeProjectDescriptor(File projectDir, GrailsProject grailsProjectDescriptor) throws MojoExecutionException {
        String description = "Grails Descriptor updated by grails-maven-plugin on " + new Date();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(projectDir, "application.properties"));
            Properties properties = new Properties();
            properties.setProperty("app.grails.version", grailsProjectDescriptor.getAppGrailsVersion());
            properties.setProperty("app.name", grailsProjectDescriptor.getAppName());
            properties.setProperty("app.version", grailsProjectDescriptor.getAppVersion());
            properties.store(fos, description);
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to write grails project descriptor.", e);
        } finally {
            IOUtil.close(fos);
        }
    }

    public GrailsPluginProject readGrailsPluginProject() throws MojoExecutionException {
        GrailsPluginProject pluginProject = new GrailsPluginProject();

        File[] files = getBasedir().listFiles(new FilenameFilter() {
            public boolean accept(File file, String s) {
                return s.endsWith(FILE_SUFFIX) && s.length() > FILE_SUFFIX.length();
            }
        });

        if(files == null || files.length != 1) {
            throw new MojoExecutionException("Could not find a plugin descriptor. Expected to find exactly one file " +
                "called FooGrailsPlugin.groovy in '" + getBasedir().getAbsolutePath() + "'.");
        }

        File descriptor = files[0];
        pluginProject.setFileName(descriptor);

        String className = descriptor.getName().substring(0, descriptor.getName().length() - ".groovy".length());
        String pluginName = GrailsNameUtils.getScriptName(GrailsNameUtils.getLogicalName(className, "GrailsPlugin"));
        pluginProject.setPluginName(pluginName);

        try {
            GroovyClassLoader classLoader = new GroovyClassLoader();
            Class clazz = classLoader.parseClass(descriptor);
            Object instance = clazz.newInstance();

            Object o = clazz.getMethod("getVersion", new Class[]{}).invoke(instance, new Object[]{});

            if (o == null) {
                throw new MojoExecutionException("getVersion() returned null!");
            }

            pluginProject.setVersion(o.toString());
        } catch (IOException e) {
            throw new MojoExecutionException("Error reading groovy file.", e);
        } catch (InstantiationException e) {
            throw new MojoExecutionException("Unable to create a new instance of the plugin configuration.", e);
        } catch (NoSuchMethodException e) {
            throw new MojoExecutionException("Unable to call getVersion() on the plugin configuration.", e);
        } catch (InvocationTargetException e) {
            throw new MojoExecutionException("Unable to call getVersion() on the plugin configuration.", e);
        } catch (IllegalAccessException e) {
            throw new MojoExecutionException("Unable to call getVersion() on the plugin configuration.", e);
        }
        return pluginProject;
    }
}
