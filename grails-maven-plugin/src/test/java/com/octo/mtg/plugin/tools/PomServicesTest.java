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

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.util.FileUtils;
import org.custommonkey.xmlunit.XMLAssert;

import java.io.File;
import java.io.FileReader;

/**
 * Tests DefaultPomServices component.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 */
public class PomServicesTest extends PlexusTestCase {

    /**
     * A minimal pom to test
     */
    private MavenProject projectTest = new MavenProject();

    /**
     * Sets up a Plexus container instance for running test.
     */
    protected void setUp() throws Exception {
        // call this to enable super class to setup a Plexus container test
        // instance and enable component lookup.
        super.setUp();
        projectTest.setModelVersion("4.0.0");
        projectTest.setGroupId("com.octo.maven.plugins");
        projectTest.setArtifactId("grails-maven-plugin-test");
        projectTest.setVersion("0.1-SNAPSHOT");
        projectTest.setPackaging("maven-plugin");

    }

    /**
     * Check that we correctly read the pom from a file.
     *
     * @throws Exception
     */
    public void testReadPom() throws Exception {
        PomServices pomServices = (PomServices) lookup(PomServices.class.getName());
        pomServices.setBasedir(getTestFile(""));
        MavenProject project = pomServices.read(getTestFile("src/test/resources/com/octo/mtg/plugin/pom-services/"));
        assertEquals(projectTest, project);
        release(pomServices);
    }

    /**
     * Check that we correctly write the project into a file.
     *
     * @throws Exception
     */
    public void testWriteproject() throws Exception {

        PomServices pomServices = (PomServices) lookup(PomServices.class.getName());
        File projectDir = getTestFile("target/write-project-test");
        FileUtils.deleteDirectory(projectDir);
        assertTrue(projectDir.mkdirs());
        pomServices.setBasedir(projectDir);
        pomServices.write(projectTest);

        File pomFile = new File(projectDir, "pom.xml");
        XMLAssert.assertXMLEqual(
            new FileReader(getTestFile("src/test/resources/com/octo/mtg/plugin/pom-services/pom.xml")),
            new FileReader(pomFile));
        pomFile.delete();
        release(pomServices);
    }
}
