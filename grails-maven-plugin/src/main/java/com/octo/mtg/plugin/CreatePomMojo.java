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
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a creates a maven 2 POM for an existing Grails project.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @description Creates a creates a maven 2 POM for on an existing Grails
 * project.
 * @goal create-pom
 * @requiresProject false
 * @requiresDependencyResolution runtime
 * @since 0.1
 */
public class CreatePomMojo extends AbstractMojo {
    private static final Pattern VAR_PATTERN = Pattern.compile("\\$\\{(.+?)\\}");

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
//    private boolean addEclipseSettings;

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
    protected GrailsServices grailsServices;

    public void execute() throws MojoExecutionException, MojoFailureException {
        grailsServices.setBasedir(basedir);
        GrailsProject grailsDescr = grailsServices.readProjectDescriptor();

        Map varSubstitutions = new HashMap();
        varSubstitutions.put("groupId", groupId);
        varSubstitutions.put("artifactId", grailsDescr.getAppName());
        varSubstitutions.put("version", grailsDescr.getAppVersion());

        // Load the template POM from the archetype (which is on the
        // classpath).
        InputStream input = getClass().getClassLoader().getResourceAsStream("archetype-resources/pom.xml");
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            writer = new BufferedWriter(new FileWriter(new File(basedir, "pom.xml")));

            // Substitute variables/tokens with the appropriate text.
            // Anything that looks like "${...}" is treated as a variable,
            // but only if the variable name is in the "varSubstitutions"
            // map does the replacement take place.
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Matcher matcher = VAR_PATTERN.matcher(line);
                StringBuffer buf = new StringBuffer(line.length());

                // Find all variable expansion patterns in this line.
                while (matcher.find()) {
                    // Get the substitution string for the variable
                    // name.
                    String sub = (String) varSubstitutions.get(matcher.group(1));
                    if (sub == null) {
                        // No substitution string found for this name,
                        // so we simply add the original text. Since
                        // the variable expansion text has a "$" in it,
                        // we need to quote it before using it as a
                        // substitution string.
                        sub = Matcher.quoteReplacement(matcher.group());
                    }
                    matcher.appendReplacement(buf, sub);
                }
                matcher.appendTail(buf);

                // Write the substituted line to the POM file.
                writer.write(buf.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to create POM file.", e);
        } finally {
            if (reader != null) try { reader.close(); } catch (IOException e) {}
            if (writer != null) try { writer.close(); } catch (IOException e) {}
        }
    }
}
