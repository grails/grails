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
 * Installs a given plugin. Either a plugin name (and optional version)
 * or a URL representing a plugin package must be specified.
 *
 * @author Peter Ledbrook
 * @version $Id$
 * @description Installs a plugin.
 * @goal install-plugin
 * @requiresProject false
 * @requiresDependencyResolution runtime
 * @since 0.4
 */
public class GrailsInstallPluginMojo extends AbstractGrailsMojo {
    /**
     * The name of the artifact to install.
     *
     * @parameter expression="${pluginName}"
     */
    private String pluginName;

    /**
     * A URL for a plugin package.
     *
     * @parameter expression="${pluginUrl}"
     */
    private String pluginUrl;

    /**
     * The version of the plugin to install.
     *
     * @parameter expression="${pluginVersion}"
     */
    private String pluginVersion;

    /**
     * Determines whether the plugin should be installed globally or not.
     *
     * @parameter expression="${isGlobal}" default-value="false"
     */
    private boolean installGlobally;

    public void execute() throws MojoExecutionException, MojoFailureException {
        // If a URL is given, we use that. Otherwise we use the plugin
        // name and optional version.
        String args = "";
        if (pluginUrl != null) {
            args = pluginUrl;
        }
        else if (pluginName != null) {
            args = pluginName;

            if (pluginVersion != null) {
                args = args + ' ' + pluginVersion;
            }
        }
        else {
            throw new MojoFailureException("Neither 'pluginName' nor 'pluginUrl' have been specified.");
        }

        if (installGlobally) {
            args += " --global";
        }

        runGrails("InstallPlugin", args, false);
    }
}
