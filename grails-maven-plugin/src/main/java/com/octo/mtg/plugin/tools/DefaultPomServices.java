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
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.WriterFactory;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * POMs utilities.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @plexus.component role="com.octo.mtg.plugin.tools.PomServices"
 */
public class DefaultPomServices extends AbstractLogEnabled implements PomServices {

    private File b;

    public void setBasedir(File basedir) {
        this.b = basedir;
    }

    public Dependency createDependency(String groupId, String artifactId, String version, String type, String scope) {
        Dependency dep = new Dependency();
        dep.setGroupId(groupId);
        dep.setArtifactId(artifactId);
        dep.setVersion(version);
        dep.setType(type);
        dep.setScope(scope);

        return dep;
    }

    public MavenProject read(File projectDirectory) throws XmlPullParserException, IOException {
        MavenXpp3Reader reader = new MavenXpp3Reader();

        return new MavenProject(reader.read(new FileReader(new File(projectDirectory, "pom.xml"))));
    }

    public void write(MavenProject project) throws MojoExecutionException {
        // MavenXpp3Writer pomWriter = new MavenXpp3Writer();
        Writer writer = null;
        Writer tempOutput = null;
        try {
            // temporary hack to add namespace declaration, not
            // supported by modello/MavenXpp3Writer
            // MavenXpp3Writer doesn't support writing the xsd
            // declaration, do it manually
            tempOutput = new StringWriter();
            project.writeModel(tempOutput);
            // pomWriter.write(tempOutput, pom);
            String pomString = tempOutput.toString();
            pomString = StringUtils.replaceOnce(pomString, "<project>",
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                        + System.getProperty("line.separator")
                        + "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">");
            writer = WriterFactory.newXmlWriter(new File(getBasedir(), "pom.xml"));
            writer.write(pomString);
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to create the pom : " + e.getMessage(), e);
        } finally {
            IOUtil.close(tempOutput);
            IOUtil.close(writer);
        }
    }

    private File getBasedir() {
        if (b != null) {
            return b;
        }

        throw new RuntimeException("The basedir has to be set before any of the service methods are invoked.");
    }
}
