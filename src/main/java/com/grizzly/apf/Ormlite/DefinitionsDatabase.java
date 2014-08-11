/*
 * Copyright (c) 2014. BBR Mobile.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.grizzly.apf.Ormlite;

/**
 * Created on 20-03-14.
 */

/**
 * Stores internal configuration values.
 */
public class DefinitionsDatabase {

    /**
     * Constants for the database name, version and password. They can't be changed at runtime.
     */

    private static final String DATABASE_NAME = "AppData.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_PASSWORD = "test";

    public static String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public static String getDatabasePassword() {
        return DATABASE_PASSWORD;
    }
}

