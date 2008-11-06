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

/**
 * @author <a href="mailto:aheritier@gmail.com">Arnaud HERITIER</a>
 * @version $Id$
 */
public class GrailsProject {

    private String appGrailsVersion;
    private String appName;
    private String appVersion;

    /**
     * The default value for app.version when it's not defined in older grails versions.
     */
    public static final String DEFAULT_APP_VERSION = "1.0-SNAPSHOT";

    public String getAppGrailsVersion() {
        return appGrailsVersion;
    }

    public void setAppGrailsVersion(String appGrailsVersion) {
        this.appGrailsVersion = appGrailsVersion;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
