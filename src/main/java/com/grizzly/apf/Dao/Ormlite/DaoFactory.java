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

package com.grizzly.apf.Dao.Ormlite;

import android.content.Context;

import com.grizzly.apf.Dao.SpecificMemoryStorage;
import com.grizzly.apf.Exceptions.GrizzlyModelException;
import com.grizzly.apf.Exceptions.GrizzlyNotFoundException;
import com.grizzly.apf.Ormlite.Model.BaseModel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

/**
 * Created on 21/03/14.
 * Creates parametrized instances of AdvancedDao
 */
public class DaoFactory<O extends OrmLiteSqliteOpenHelper> {

    private Class<O> schemaClass;

    private SpecificMemoryStorage daoStorage = new SpecificMemoryStorage();

    private String databaseName = "";

    public DaoFactory(Class<O> schemaClass) {
        this.schemaClass = schemaClass;
    }

    public DaoFactory(Class<O> schemaClass, Context context) {
        this.schemaClass = schemaClass;
    }

    public void setDatabaseName(String s){
        if( !(s==null || "".trim().equalsIgnoreCase(s)) ){
            if( !(databaseName==null || "".trim().equalsIgnoreCase(databaseName)) ){
                databaseName = s;
            }
        }
    }

    private String getDatabaseName(){
        if( !(databaseName==null || "".trim().equalsIgnoreCase(databaseName)) ){
            return null;
        }
        return databaseName;
    }

    /**
     * Allows to get singleton instances of AdvancedDao. Creates an specific AdvancedDao
     * for each BaseModel class in the given schema.
     *
     * @param entityClass a valid BaseModel subclass.
     * @param <T>         Generic class operator. Represents the BaseModel subclass.
     * @param <C>         Generic class operator. Represents the BaseModel subclass ID field's class.
     * @return an AdvancedDao parametrized
     * as AdvancedDao<T extends BaseModel, BaseModel.getIdClass, O extends OrmLiteSqliteOpenHelper>
     */
    public <T extends BaseModel, C> AdvancedDao<T, C, O> getSingleDaoInstance(Class<T> entityClass) {

        Class<C> idClass = null;
        try {

            idClass = entityClass.newInstance().getIdClass();

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        String key = entityClass.getSimpleName() + "-" + schemaClass.getSimpleName();

        if (daoStorage.find(key)) {
            return (AdvancedDao<T, C, O>) daoStorage.getEntity(key, AdvancedDao.class);
        } else {
            AdvancedDao<T, C, O> dao = new AdvancedDao<>(entityClass, idClass, schemaClass);
            if(getDatabaseName()!=null){
                dao.setDatabaseName(databaseName);
            }
            daoStorage.create(key, dao);
            return dao;
        }
    }

    /**
     * Return an instance of AdvancedDao so one of this methods can be called. If the entity's class is not supported,
     * throws a GrizzlyModelException.
     *
     * @param entity  the entity to provide the DAO.
     * @param context the application context.
     * @param <T>     a subclass of BaseModel.
     * @return an AdvancedDao instance.
     * @throws com.grizzly.apf.Exceptions.GrizzlyModelException
     */
    public <T extends BaseModel, C> AdvancedDao<T, C, O> getProperDao(T entity, Context context) throws GrizzlyModelException {

        try {
            AdvancedDao<T, C, O> dao = (AdvancedDao<T, C, O>) this.getSingleDaoInstance(entity.getClass());
            dao.setSource(entity);
            dao.setContext(context);
            return dao;
        } catch (Exception e) {
            throw new GrizzlyModelException();
        }
    }

    public <T extends BaseModel, C> AdvancedDao<T, C, O> getProperDao(Class<T> entityClass, Context context) throws GrizzlyModelException {

        try {
            AdvancedDao<T, C, O> dao = this.getSingleDaoInstance(entityClass);
            dao.setContext(context);
            return dao;
        } catch (Exception e) {
            throw new GrizzlyModelException();
        }
    }

    /**
     * Performs a search and returns a T entity matching the desired results. If there isn't any results, then throws a
     * GrizzlyNotFoundException. If the T class provided is not supported, throws a GrizzlyModelException.
     *
     * @param entity  the entity with the ID to be searched.
     * @param context application context.
     * @param <T>     A subclass of BaseModel.
     * @return a T object with the search result.
     * @throws com.grizzly.apf.Exceptions.GrizzlyModelException
     * @throws com.grizzly.apf.Exceptions.GrizzlyNotFoundException
     */
    public <T extends BaseModel, C> T getProperDaoResponse(T entity, Context context) throws GrizzlyModelException, GrizzlyNotFoundException {

        try {
            AdvancedDao<T, C, O> dao = this.getProperDao(entity, context);
            if (dao.find()) {
                return dao.getSource();
            } else {
                throw new GrizzlyNotFoundException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GrizzlyModelException();
        }
    }
}
