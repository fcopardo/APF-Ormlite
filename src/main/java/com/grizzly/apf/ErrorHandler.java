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

package com.grizzly.apf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 20-03-14.
 * The error management class.
 */
public class ErrorHandler {

    /**
     * Error Codes
     */
    public static final String errorDao = "Error-Dao";
    public static final String errorDataManager = "Error-DataManager";
    public static final String argumentError = "Expected another class";

    /**
     *
     */

    private static List<String[]> errorMessages = new ArrayList<String[]>();

    /**
     * Returns the stored error messages.
     * @return a List of string arrays.
     */
    public static List<String[]> getErrorMessages() {
        return errorMessages;
    }

    /**
     * Stores a new error message.
     * @param error the error to be stored.
     * @param sourceClass the class where the error was produced.
     * @param errorType the error code.
     */
    public static void addError(String error, Class sourceClass, String errorType) {
        String[] errorMessage = new String[3];
        errorMessage[0] = error;
        errorMessage[1] = sourceClass.getCanonicalName();
        errorMessage[2] = errorType;

        errorMessages.add(errorMessage);
    }

}
