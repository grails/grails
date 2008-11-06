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

import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 * @plexus.component role="com.octo.mtg.plugin.tools.MojoServices"
 */
public class DefaultMojoServices extends AbstractLogEnabled implements MojoServices {

    public int executeCommandLine(Commandline cl, InputStream systemIn, OutputStream systemOut, OutputStream systemErr)
        throws CommandLineException {
        if (cl == null) {
            throw new IllegalArgumentException("cl cannot be null.");
        }

        Process p = cl.execute();
        RawStreamPumper inputFeeder = null;
        if (systemIn != null) {
            inputFeeder = new RawStreamPumper(systemIn, p.getOutputStream());
        }
        RawStreamPumper outputPumper = new RawStreamPumper(p.getInputStream(), systemOut);
        RawStreamPumper errorPumper = new RawStreamPumper(p.getErrorStream(), systemErr);
        if (inputFeeder != null) {
            inputFeeder.start();
        }
        outputPumper.start();
        errorPumper.start();
        try {
            int returnValue = p.waitFor();
            if (inputFeeder != null) {
                inputFeeder.setDone();
            }
            outputPumper.setDone();
            errorPumper.setDone();

            return returnValue;
        } catch (InterruptedException ex) {
            throw new CommandLineException("Error while executing external command, process killed.", ex);
        } finally {
            errorPumper.closeInput();
            outputPumper.closeInput();
            if (inputFeeder != null) {
                inputFeeder.closeOutput();
            }
        }
    }
}
