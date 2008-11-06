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
 * Cleans a Grails project and jars in lib directory.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @description Cleans a Grails project and jars in lib directory.
 * @goal maven-clean
 * @phase clean
 * @requiresProject true
 * @requiresDependencyResolution
 * @since 0.1
 */
public class MvnCleanMojo extends AbstractGrailsMojo {

    /**
     * Executes the MvnWarMojo on the current project.
     *
     * @throws MojoExecutionException if an error occured while building the webapp
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        runGrails("Clean");
    }
}
