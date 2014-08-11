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

package com.grizzly.apf.Definitions;

/**
 * Created on 09-04-14.
 * Defines the strategies available. Works along with the StorageStrategy enum type. Any new strategies
 * must be added on both files.
 */
public class DefinitionsStrategies {

    /*1 for Local or Memory storage, 2 for Database storage, 3 for WebService storage, 4 for Local and Database storage,
    * 5 for Local and WebService storage, 6 for Database and WebService storage and 7 for all of them.
    * */

    public static int STRATEGY_DONT_STORE = 0;
    public static int STRATEGY_MEMORY = 1;
    public static int STRATEGY_DATABASE = 2;
    public static int STRATEGY_WEBSERVICE = 3;
    public static int STRATEGY_MEMORY_AND_DATABASE = 4;
    public static int STRATEGY_MEMORY_AND_WEBSERVICE = 5;
    public static int STRATEGY_DATABASE_AND_WEBSERVICE = 6;
    public static int STRATEGY_ALL = 7;

    /**
     * Evaluates if a given number is a valid strategy.
     * @param strategy the int representing the strategy.
     * @return true or false.
     */
    public static boolean isStrategy(int strategy) {
        if (strategy < STRATEGY_ALL && strategy >= STRATEGY_DONT_STORE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Creates an StorageStrategy enum from an int. It's intended to be used with the StorageStrategy
     * int field of the BaseModel class.
     * @param strategy the int representing the strategy.
     * @return a valid StorageStrategy enum.
     */
    public static StorageStrategy selectStrategy(int strategy) {


        if(!isStrategy(strategy)){
            strategy = STRATEGY_MEMORY;
        }

        StorageStrategy storageStrategy = StorageStrategy.MEMORY;

        switch(strategy) {
            case 0:
                storageStrategy = StorageStrategy.DONT_STORE;
                break;
            case 1:
                storageStrategy = StorageStrategy.MEMORY;
                break;
            case 2:
                storageStrategy = StorageStrategy.DATABASE;
                break;
            case 3:
                storageStrategy = StorageStrategy.WEBSERVICE;
                break;
            case 4:
                storageStrategy = StorageStrategy.MEMORY_DATABASE;
                break;
            case 5:
                storageStrategy = StorageStrategy.MEMORY_WEBSERVICE;
                break;
            case 6:
                storageStrategy = StorageStrategy.DATABASE_WEBSERVICE;
                break;
            case 7:
                storageStrategy = StorageStrategy.ALL;
                break;
            default:break;
        }
        return storageStrategy;
    }

}
