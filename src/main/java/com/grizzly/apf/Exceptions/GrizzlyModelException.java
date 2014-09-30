/*
 * Copyright (c) 2014. Fco Pardo Baeza.
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

package com.grizzly.apf.Exceptions;

/**
 * Created on 04-04-14. An exception to be thrown when a given parameter is not supported.
 */
public class GrizzlyModelException extends Exception{

    public GrizzlyModelException() { super("The provided model is not available in the local database schema"); }
    public GrizzlyModelException(String message) { super(message); }
    public GrizzlyModelException(String message, Throwable cause) { super(message, cause); }
    public GrizzlyModelException(Throwable cause) { super(cause); }

}
