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

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.IOException;

public interface PomServices {

    /**
     * Sets the basedir that all commands are executed from
     *
     * @since 0.3
     */
    void setBasedir(File basedir);

    /**
     * @param groupId
     * @param artifactId
     * @param version
     * @param type
     * @param scope
     * @return
     */
    public Dependency createDependency(String groupId, String artifactId, String version, String type, String scope);

    /**
     * Read a maven pom from a file.
     *
     * @param projectDirectory
     *                The parent directory where the pom must be write
     * @return A POM for maven
     * @throws Exception
     *                 if a problem occurs
     */
    public MavenProject read(File projectDirectory) throws XmlPullParserException, IOException;

    /**
     * Write a maven POM in a file.
     *
     * @param project
     *                The pom to write
     * @throws Exception
     *                 If a problem occurs
     */
    public void write(MavenProject project) throws MojoExecutionException;
}
