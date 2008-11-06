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

import org.apache.maven.project.validation.ModelValidator;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusTestCase;

/**
 * Tests GrailsServices component.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 */
public class GrailsServicesTest extends PlexusTestCase {

    /**
     * A minimal grails descriptor to test
     */
    private GrailsProject grailsDescriptorTest = new GrailsProject();

    /**
     * Sets up a Plexus container instance for running test.
     */
    protected void setUp() throws Exception {

        // call this to enable super class to setup a Plexus container test
        // instance and enable component lookup.
        super.setUp();
        grailsDescriptorTest.setAppGrailsVersion("0.5.6");
        grailsDescriptorTest.setAppName("a-grails-app");
        grailsDescriptorTest.setAppVersion("1.0-SNAPSHOT");

    }

    public void testPomIsValid() throws Exception {
        ModelValidator modelValidator = (ModelValidator) lookup(ModelValidator.class.getName());
        GrailsServices grailsServices = (GrailsServices) lookup(GrailsServices.class.getName());

        grailsServices.setBasedir(getTestFile(""));

        MavenProject pom = grailsServices.createPOM("a.group", grailsDescriptorTest, "com.octo.mtg",
            "grails-maven-plugin", "1.0");

        assertEquals(0, modelValidator.validate(pom.getModel()).getMessageCount());
        release(modelValidator);
        release(grailsServices);
    }
}
