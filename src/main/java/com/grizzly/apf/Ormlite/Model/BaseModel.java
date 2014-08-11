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
package com.grizzly.apf.Ormlite.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grizzly.apf.Definitions.DefinitionsStrategies;
import com.j256.ormlite.field.DatabaseField;

/**
 * Base model entity with common members.
 */

public abstract class BaseModel<T> {
    /**
     * Constants for Ormlite. They will represent the name of each field in the database.
     */
    @JsonIgnore
    public static final String STORAGE_STRATEGY = "storage_strategy";
    @JsonIgnore
    public static final String PATH_TO_FILES = "path_to_files";

    /**
     * Generic abstract method for accessing to ID field. The subclasses must implement
     * it as an accessor to the ID field.
     *
     * @return
     */

    public abstract T getId();
    public abstract void setId(T id);

    @JsonIgnore
    protected final Class<T> idClass;


    /**
     * Class members
     * StorageStrategy determines the entity's storage strategy.
     * encryptionActive determines if the entity's fields are going to be encrypted.
     * pathToFiles stores the relative path to any files the entity stores in the local file system.
     */

    @JsonIgnore
    @DatabaseField(columnName = STORAGE_STRATEGY)
    private int StorageStrategy;

    @JsonIgnore
    @DatabaseField(columnName = PATH_TO_FILES)
    private String pathToFiles;

    public BaseModel(Class<T> idClass) {
        this.idClass = idClass;
    }

    public Class<T> getIdClass() {
        return idClass;
    }

    /**
     * Determines the storage strategy of the instance.
     *
     * @param UpdateStrategy 1 for Local or Memory storage, 2 for Database storage, 3 for WebService storage,
     *                       4 for Local and Database storage, 5 for Local and WebService storage, 6 for Database and WebService storage
     *                       and 7 for all of them.
     */
    public void setStorageStrategy(int UpdateStrategy) {

        if (!DefinitionsStrategies.isStrategy(UpdateStrategy)) {
            this.StorageStrategy = DefinitionsStrategies.STRATEGY_MEMORY;
        }
        else {
            this.StorageStrategy = UpdateStrategy;
        }
    }

    public int getStorageStrategy() {
        return StorageStrategy;
    }

    public String getPathToFiles() {
        return pathToFiles;
    }

    public void setPathToFiles(String PathToFiles) {
        pathToFiles = PathToFiles;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (!getId().equals((this.getClass().cast(o)).getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
