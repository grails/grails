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
package org.grails.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Runs a Grails application's functional tests.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @description Runs a Grails application's functional tests.
 * @goal maven-functional-test
 * @phase integration-test
 * @requiresProject true
 * @since 1.0
 */
public class MvnFunctionalTestMojo extends AbstractGrailsMojo {

    /**
     * Set this to 'true' to bypass functional tests entirely. Its use is
     * NOT RECOMMENDED, but quite convenient on occasion.
     *
     * @parameter expression="${grails.test.skip}"
     * @since 0.3
     */
    private boolean skip;

    /**
     * Set this to 'true' to bypass functional tests entirely. Its use is
     * NOT RECOMMENDED, but quite convenient on occasion.
     *
     * @parameter expression="${maven.test.skip}"
     * @since 0.3
     */
    private Boolean mavenSkip;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            return;
        }

        if (mavenSkip != null && mavenSkip.booleanValue()) {
            return;
        }

        runGrails("TestApp", "--functional", true);
    }
}
