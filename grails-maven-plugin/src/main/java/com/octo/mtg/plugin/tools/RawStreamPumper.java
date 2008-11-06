package com.octo.mtg.plugin.tools;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.codehaus.plexus.util.IOUtil;

import java.io.InputStream;
import java.io.OutputStream;

public class RawStreamPumper extends Thread {
    byte buffer[] = new byte[256];

    boolean done;

    private InputStream in;

    private OutputStream out;

    boolean poll;

    public RawStreamPumper(InputStream in, OutputStream out) {

	this.in = in;
	this.out = out;
	this.poll = false;

    }

    public RawStreamPumper(InputStream in, OutputStream out, boolean poll) {

	this.in = in;
	this.out = out;
	this.poll = poll;

    }

    public void closeInput() {

	IOUtil.close(in);

    }

    public void closeOutput() {

	IOUtil.close(out);

    }

    public void run() {

	try {
	    if (poll) {
		while (!done) {
		    if (in.available() > 0) {
			int i = in.read(buffer);
			if (i != -1) {
			    out.write(buffer, 0, i);
			    out.flush();
			} else {
			    done = true;
			}
		    } else {
			Thread.sleep(1);
		    }
		}
	    } else {
		int i = in.read(buffer);
		while (i != -1 && !done) {
		    if (i != -1) {
			out.write(buffer, 0, i);
			out.flush();
		    } else {
			done = true;
		    }
		    i = in.read(buffer);
		}
	    }
	} catch (Throwable e) {
	    // Catched everything
	} finally {
	    done = true;
	}

    }

    public void setDone() {

	done = true;

    }
}
