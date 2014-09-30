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
package com.grizzly.apf.Ormlite;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides a singleton instance of DBHelperOrmlite. Allows to connect to any database using a class extending
 * com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper as helper (schema and connection provider).
 */ 
public class DBManager {

    /**
     * map: a container for an instance of a subclass of com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper.
     * Allows to simulate the singleton instance.
     */
    private static Map<String, Object> map;

    /**
     * Singleton instance of the container.
     */
    private static void createSingleInstance() {
        if(map == null){
            map = new LinkedHashMap<>();
        }
    }

    /**
     * Allows to store a helper in the container map.
     * @param key
     * @param value
     * @param <T>
     */
    private static<T extends OrmLiteSqliteOpenHelper> void create(String key, T value) {
        createSingleInstance();
        map.put(key, value);
    }

    /**
     * Checks if a helper is in the map.
     * @param key
     * @return
     */
    private static boolean find(String key) {
        try {
            return map.containsKey(key);
        }
        catch(NullPointerException e){
            return false;
        }
    }

    /**
     * Returns a helper contained in the map. Type safe operation.
     * @param key
     * @param theClass
     * @param <T>
     * @return
     */
    private static <T extends OrmLiteSqliteOpenHelper> T getHelper(String key, Class<T> theClass) {
        createSingleInstance();
        return (T)map.get(key);
    }

    public static <T extends OrmLiteSqliteOpenHelper> void dropHelper(String key, Class<T> theClass) {
        createSingleInstance();

        if(find(key)){
            if(getHelper(key, theClass).isOpen()){
                getHelper(key, theClass).close();
            }
            map.remove(key);
        }
    }

    /**
     * Simulates a singleton instance of a database helper, therefore creating and opening the database and allowing
     * to use it.
     * @param context a valid application context.
     * @param password the password of the database.
     * @param schemaClass a subclass of com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
     *        implementing the database schema class (Example: ORMSchema.class)
     * @param <T>
     * @return a valid database helper to connect with a database described in schemaClass (Connection)
     */
    public static <T extends OrmLiteSqliteOpenHelper> T getHelper(Context context, Class<T> schemaClass) {

        T helper=null;

        if(find(schemaClass.getSimpleName()+DefinitionsDatabase.getDatabaseName())) {
            helper = getHelper(schemaClass.getSimpleName()+DefinitionsDatabase.getDatabaseName(), schemaClass);
        }
        else{

            try{
                helper = OpenHelperManager.getHelper(context, schemaClass);
            }
            catch(IllegalStateException e) {
                OpenHelperManager.releaseHelper();
                OpenHelperManager.setHelper(helper);
                helper = OpenHelperManager.getHelper(context, schemaClass);
            }
            create(schemaClass.getSimpleName()+DefinitionsDatabase.getDatabaseName(), helper);
        }
        helper.getWritableDatabase();
        return helper;
    }


    /**
     * Allows to destroy the helper
     */
    public static void DestroyHelper() {
        if (map != null) {
            OpenHelperManager.releaseHelper();
            map = null;
        }
    }

    protected void onDestroy() {
        if (map != null) {
            OpenHelperManager.releaseHelper();
            map = null;
        }
    }
}
