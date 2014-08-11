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

package com.grizzly.apf.Dao;

/**
 * A generic container to allow generic instantiation of non generic object.
 *
 * @param <T> The class for the instantiated objects.
 * @author
 */
public class Container<T> {

    private Class<T> clazz;

    public Container(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * The warning must be supressed, since the method operates with generics.
     *
     * @param instance
     */
    @SuppressWarnings("unchecked")
    public Container(T instance) {
        this.clazz = (Class<T>) instance.getClass();
    }

    public T createContents() throws IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }

    public Class getClazz() {
        return clazz;
    }
}

