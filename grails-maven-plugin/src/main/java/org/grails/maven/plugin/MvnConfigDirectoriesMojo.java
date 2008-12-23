/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

import java.io.File;

/**
 * Set sources/tests directories to be compatible with the directories layout used by grails.
 *
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @description Set sources/tests directories to be compatible with the directories layout used by grails.
 * @goal config-directories
 * @phase generate-sources
 * @since 0.3
 */
public class MvnConfigDirectoriesMojo extends AbstractGrailsMojo {

    /*
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
//    private MavenProject project;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        File projectDir = this.project.getBasedir();

        // Get all the sub-directories of the "plugins" dir.
        File pluginsDir = new File(projectDir, "plugins");
//        File[] dirs = pluginsDir.listFiles(new FileFilter() {
//            public boolean accept(File file) {
//                return file.isDirectory();
//            }
//        });

        // Add the "src/java" directory in each plugin to the compiler's
        // source.
//        for (int i = 0; i < dirs.length; i++) {
//            this.project.addCompileSourceRoot(new File(dirs[i], "src/java").getAbsolutePath());
//        }

        // Add sources directories
        this.project.addCompileSourceRoot((new File(projectDir, "src/java")).getAbsolutePath());
        // Add tests directories
//        this.project.addTestCompileSourceRoot((new File(projectDir, "test/unit")).getAbsolutePath());
//        this.project.addTestCompileSourceRoot((new File(projectDir, "test/integration")).getAbsolutePath());
        // Change output dir
//        this.project.getModel().getBuild().setOutputDirectory("web-app/WEB-INF/classes");
    }
}
