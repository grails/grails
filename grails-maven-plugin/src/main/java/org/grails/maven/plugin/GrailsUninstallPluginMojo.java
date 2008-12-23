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
 * Uninstalls a named plugin.
 *
 * @author Peter Ledbrook
 * @version $Id$
 * @description Uninstalls a plugin.
 * @goal uninstall-plugin
 * @requiresProject false
 * @requiresDependencyResolution runtime
 * @since 0.4
 */
public class GrailsUninstallPluginMojo extends AbstractGrailsMojo {
    /**
     * The name of the artifact to install.
     *
     * @parameter expression="${pluginName}"
     */
    private String pluginName;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (pluginName == null) {
            throw new MojoFailureException("'pluginName' must be specified.");
        }

        runGrails("UninstallPlugin", pluginName, false);
    }
}
