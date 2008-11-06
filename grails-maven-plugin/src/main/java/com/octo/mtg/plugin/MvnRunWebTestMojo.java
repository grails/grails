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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Runs all of the Web tests against a Grails application. <b> By default, this
 * mojo used in the grails-app lifecycle is deactivated. </b>
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @description Runs all of the Web tests against a Grails application. By
 * default, this mojo used in the grails-app lifecycle is
 * deactivated.
 * @goal maven-run-webtest
 * @requiresProject true
 * @since 0.3
 */
public class MvnRunWebTestMojo extends AbstractGrailsMojo {

    /**
     * Set this to 'true' to enable web tests. Used to enable them in an
     * integration profile for example.
     *
     * @parameter expression="${grails.webtest.enable}"
     * @since 0.3
     */
    private boolean enable;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (enable)
            getGrailsServices().launchGrails(grailsHome, env, "run-webtest");
    }
}
