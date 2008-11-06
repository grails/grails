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

import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.InputStream;
import java.io.OutputStream;

public interface MojoServices {

    /**
     * @param cl
     * @param systemIn
     * @param systemOut
     * @param systemErr
     * @return
     * @throws CommandLineException
     */
    public int executeCommandLine(Commandline cl, InputStream systemIn, OutputStream systemOut, OutputStream systemErr)
	    throws CommandLineException;
}
