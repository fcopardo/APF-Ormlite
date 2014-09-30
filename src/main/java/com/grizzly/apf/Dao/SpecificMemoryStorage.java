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

package com.grizzly.apf.Dao;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created on 13-06-14.
 * RAM storage system.
 * Allows to create a MemoryStorage for specific data.
 */
public class SpecificMemoryStorage {

    private Map<String, Object> storedData = new LinkedHashMap<>();;

    /**
     * Adds a value to the RAM storage. Any values to be added would be indexed using the key parameter.
     * Is paramount to remember it so the value could be read, updated or deleted. When reading the value a
     * cast operation could be necessary. In runtime, T will be translated to the value's class.
     * @param key the identifier we are going to use.
     * @param value the value to be stored.
     */
    public <T> void create(String key, T value) {
        storedData.put(key, value);
    }

    /**
     * Deleted a value from the RAM storage, using the key parameter to search.
     * @param key the key identifying our value.
     */
    public void delete(String key) {
        storedData.remove(key);
    }

    /**
     * Indicates if there is a value in the storedData with a key matching the parameter.
     * @param key the key to be searched.
     * @return true or false.
     */
    public boolean find(String key) {
        try {
            return storedData.containsKey(key);
        }
        catch(NullPointerException e){
            return false;
        }
    }

    /**
     * Return a value from our storage. The value must be casted to the original type in order to have access to
     * its properties and methods.
     * @param key the key to be searched.
     * @return
     */
    public Object getEntity(String key) {
        return storedData.get(key);
    }

    /**
     * Return a value from our storage. The value will be casted to the provided class.
     * @param key the key to be searched.
     * @param theClass a class to cast the value.
     * @return the value, casted to the provided class.
     */
    public <T> T getEntity(String key, Class<T> theClass) {
        return (T) storedData.get(key);
    }

    /**
     * Updates a value. Since the collection is a storedData, the update works exactly like the create method.
     * @param key The value to be searched.
     * @param value The new value.
     */
    public <T> void update(String key, T value) {
        create(key, value);
    }

    /**
     * Returns the keySet of the storedData for allowing iteration in external methods.
     * @return the storedData's keySet.
     */
    public java.util.Set<String> getEntrySet(){
        return storedData.keySet();
    }
}
