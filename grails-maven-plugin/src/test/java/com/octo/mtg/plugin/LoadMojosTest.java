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

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

/**
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 */
public class LoadMojosTest extends AbstractMojoTestCase {

    private void mojoTest(String pluginConfig, String mojoName, Class mojoClass) throws Exception {
        File testPom = getTestFile("src/test/resources/com/octo/mtg/plugin/" + pluginConfig);
        Object mojo = lookupMojo(mojoName, testPom);
        assertNotNull(mojo);
        assertEquals(mojo.getClass(), mojoClass);
        release(mojo);
    }

    public void testLoadCreatePomMojoLookup() throws Exception {
        mojoTest("create-pom/plugin-config.xml", "create-pom", CreatePomMojo.class);
    }

    public void testLoadGrailsCleanMojoLookup() throws Exception {
        mojoTest("grails-clean/plugin-config.xml", "clean", GrailsCleanMojo.class);
    }

    public void testLoadGrailsCreateControllerMojoLookup() throws Exception {
        mojoTest("grails-create-controller/plugin-config.xml", "create-controller", GrailsCreateControllerMojo.class);
    }

    public void testLoadGrailsCreateDomainClassMojoLookup() throws Exception {
        mojoTest("grails-create-domain-class/plugin-config.xml", "create-domain-class", GrailsCreateDomainClassMojo.class);
    }

    public void testLoadGrailsCreateIntegrationTestMojoLookup() throws Exception {
        mojoTest("grails-create-integration-test/plugin-config.xml", "create-integration-test", GrailsCreateIntegrationTestMojo.class);
    }

    public void testLoadGrailsCreateScriptMojoLookup() throws Exception {
        mojoTest("grails-create-script/plugin-config.xml", "create-script", GrailsCreateScriptMojo.class);
    }

    public void testLoadGrailsCreateServiceMojoLookup() throws Exception {
        mojoTest("grails-create-service/plugin-config.xml", "create-service", GrailsCreateServiceMojo.class);
    }

    public void testLoadGrailsCreateTaglibMojoLookup() throws Exception {
        mojoTest("grails-create-taglib/plugin-config.xml", "create-taglib", GrailsCreateTaglibMojo.class);
    }

    public void testLoadGrailsCreateUnitTestMojoLookup() throws Exception {
        mojoTest("grails-create-unit-test/plugin-config.xml", "create-unit-test", GrailsCreateUnitTestMojo.class);
    }

    public void testLoadGrailsCreateWebTestMojoLookup() throws Exception {
        mojoTest("grails-create-web-test/plugin-config.xml", "create-web-test", GrailsCreateWebTestMojo.class);
    }

    public void testLoadGrailsGenerateAllMojoLookup() throws Exception {
        mojoTest("grails-generate-all/plugin-config.xml", "generate-all", GrailsGenerateAllMojo.class);
    }

    public void testLoadGrailsGenerateControllerMojoLookup() throws Exception {
        mojoTest("grails-generate-controller/plugin-config.xml", "generate-controller", GrailsGenerateControllerMojo.class);
    }

    public void testLoadGrailsGenerateViewsMojoLookup() throws Exception {
        mojoTest("grails-generate-views/plugin-config.xml", "generate-views", GrailsGenerateViewsMojo.class);
    }

    public void testLoadGrailsInstallTemplatesMojoLookup() throws Exception {
        mojoTest("grails-install-templates/plugin-config.xml", "install-templates", GrailsInstallTemplatesMojo.class);
    }

    public void testLoadGrailsPackageMojoLookup() throws Exception {
        mojoTest("grails-package/plugin-config.xml", "package", GrailsPackageMojo.class);
    }

    public void testLoadGrailsRunAppMojoLookup() throws Exception {
        mojoTest("grails-run-app/plugin-config.xml", "run-app", GrailsRunAppMojo.class);
    }

    public void testLoadGrailsRunAppHttpsMojoLookup() throws Exception {
        mojoTest("grails-run-app-https/plugin-config.xml", "run-app-https", GrailsRunAppHttpsMojo.class);
    }

    public void testLoadGrailsRunWebTestMojoLookup() throws Exception {
        mojoTest("grails-run-webtest/plugin-config.xml", "run-webtest", GrailsRunWebTestMojo.class);
    }

    public void testLoadGrailsTestAppMojoLookup() throws Exception {
        mojoTest("grails-test-app/plugin-config.xml", "test-app", GrailsTestAppMojo.class);
    }

    public void testLoadGrailsWarMojoLookup() throws Exception {
        mojoTest("grails-war/plugin-config.xml", "war", GrailsWarMojo.class);
    }

    public void testLoadMavenCleanMojoLookup() throws Exception {
        mojoTest("maven-clean/plugin-config.xml", "maven-clean", MvnCleanMojo.class);
    }

    public void testLoadMavenRunWebTestMojoLookup() throws Exception {
        mojoTest("maven-run-webtest/plugin-config.xml", "maven-run-webtest", MvnRunWebTestMojo.class);
    }

    public void testLoadMavenTestAppMojoLookup() throws Exception {
        mojoTest("maven-test-app/plugin-config.xml", "maven-test", MvnTestMojo.class);
    }

    public void testLoadMavenValidateMojoLookup() throws Exception {
        mojoTest("maven-validate/plugin-config.xml", "validate", MvnValidateMojo.class);
    }

    public void testLoadMavenWarMojoLookup() throws Exception {
        mojoTest("maven-war/plugin-config.xml", "maven-war", MvnWarMojo.class);
    }
}
