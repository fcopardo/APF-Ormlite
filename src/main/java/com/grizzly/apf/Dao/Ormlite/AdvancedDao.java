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
import com.grizzly.apf.Dao.Container;
import com.grizzly.apf.ErrorHandler;
import com.grizzly.apf.Ormlite.DBManager;
import com.grizzly.apf.Ormlite.Model.BaseModel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * General DAO class. Allows to create DAOs to any entity extending BaseModel, in runtime.
 *
 * @param <T> the entity class to be embedded in the DAO.
 * @param <C> the entity's key class
 * @param <O> the database helper class.
 * @author
 */

public class AdvancedDao<T extends BaseModel, C, O extends OrmLiteSqliteOpenHelper> {
    /**
     * entityClass: a reference to the entity's class.
     * idClass: a reference to the id's class.
     * source: a instance of an entity, embedded into the DAO.
     * id: the id or primary key element of the "source"
     * dao: base generic dao to access the ormLite API.
     * context: an android application context.
     * helper: a class in charge of providing the database connection and schema.
     */
    private final Class<T> entityClass;
    private final Class<C> idClass;
    private final Class<O> helperClass;
    private T source;
    protected C id;
    private Dao<T, C> dao;
    private Dao plainDao;
    private Context context;
    private String routeToId;
    private O helper = null;
    private String databaseName = "";


    public AdvancedDao(Class<T> entityClass, Class<C> idClass, Class<O> helperClass) {
        this.entityClass = entityClass;
        this.idClass = idClass;
        this.helperClass = helperClass;

        try {
            this.source = entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public AdvancedDao(Class<T> entityClass, Class<C> idClass, Context c, Class<O> helperClass) {
        this.entityClass = entityClass;
        this.idClass = idClass;
        this.helperClass = helperClass;
        this.context = c;
        this.helper = getHelper();

        try {
            this.source = entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setDatabaseName(String s) {
        if (!(s == null || "".trim().equalsIgnoreCase(s))) {
            if (!(databaseName == null || "".trim().equalsIgnoreCase(databaseName))) {
                databaseName = s;
            }
        }
    }

    private String getDatabaseName() {
        if (!(databaseName == null || "".trim().equalsIgnoreCase(databaseName))) {
            return null;
        }
        return databaseName;
    }

    public void setContext(Context c) {
        context = c;
    }

    public Context getContext() {
        return context;
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public Dao<T, C> getMyDao() throws SQLException {

        return (Dao<T, C>) getHelper().getDao(entityClass);
    }

    public void setMyDao(Dao<T, C> dao) {
        this.dao = dao;
    }

    /**
     * Allows to get the helper used in this DAO.
     *
     * @return a database helper based in the helperClass (O) argument
     */
    public O getHelper() {
        if (helper == null) {

            helper = DBManager.getHelper(context.getApplicationContext(), helperClass);

        }
        return helper;
    }

    public void setHelper(O helper) {
        this.helper = helper;
    }

    /**
     * Adds the error to the ErrorHandler so it can be reported later.
     *
     * @param t the error message.
     */
    public void handleError(String t) {
        ErrorHandler.addError(t, entityClass, ErrorHandler.errorDao);
    }

    /**
     * Returns the entity affected by the CRUD operations. If neither an operation has been done nor an entity has been set, it will return a new instance.
     * @return
     */
    public T getSource() {
        if(source == null){
            try {
                source = entityClass.newInstance();
            } catch (InstantiationException|IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        return (T)source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public String getRouteToId() {
        return routeToId;
    }

    public void setRouteToId(String routeToId) {
        this.routeToId = routeToId;
    }

    /**
     * Returns a boolean.
     * Creates an instance of T in the database, using source as the object instance.
     *
     * @return true if the instance is created successfully.
     */
    public boolean create() {
        try {
            /*
             * When an Object-To-Object mapper is found, it will be possible to use this kind of declarations.
             * I would like to wait to see if possible, because it could create a lot of flexibility.
             * Container objectFactory = new Container(this.source); T n = (T)
			 * objectFactory.createContents(); mapper.map(this.source, n);
			 */
            //setMyDao(getMyDao());
            setMyDao(getMyDao());
            getMyDao().create(this.source);
            return true;
        } catch (Exception db) {
            this.handleError("Error while creating " + entityClass.getClass().toString());
            db.printStackTrace();
            return false;
        }
    }

    /**
     * Non-functional. Requires a valid mapper.
     * Returns a boolean.
     * Creates an instance of T in the database, using o as the object instance.
     *
     * @param o an object to be instantiated into the database.
     * @return true if the instance is created successfully.
     */
    private boolean createFromObject(Object o) {
        try {
            Container objectFactory = new Container(this.source);
            T n = (T) objectFactory.createContents();
            // mapper.map(o, n);
            setMyDao(getMyDao());
            getMyDao().create(n);
            return true;
        } catch (Exception db) {
            this.handleError("Error while creating " + entityClass.getClass().toString());
            db.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a boolean.
     * Creates an instance of T in the database, using entity as the object instance.
     *
     * @param entity an entity object to be instantiated.
     * @return true if the instance is created successfully.
     */
    public boolean create(T entity) {
        try {
            setMyDao(getMyDao());
            if(getMyDao().create(entity)==1) return true;
            return false;
        } catch (Exception db) {
            this.handleError("Error while creating " + source.getClass().getCanonicalName());
            this.handleError("The creation operation in the class: " + source.getClass().getCanonicalName() + " was impossible for the given entity");
            db.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a boolean.
     * Looks for an object instance into the database.
     *
     * @param id the ID of the object to be searched.
     * @return true if the instance is found.
     */
    public boolean find(C id) {
        try {
            setMyDao(getMyDao());
            if(getMyDao().queryForId(id)==null){
                return false;
            }
            else{
                this.setSource(getMyDao().queryForId(id));
                return true;
            }
        } catch (Exception db) {
            this.handleError("The getEntity operation in the class: " + source.getClass().getCanonicalName() + " was impossible for the value: " + id);
            db.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a boolean.
     * Looks for an object instance into the database, using the embedded object.
     *
     * @return true if the instance is found.
     */
    public boolean find() {
        C oldId = (C) source.getId();
        C id = (C)source.getId();
        try {
            setMyDao(getMyDao());
            if(getMyDao().queryForId(id)==null){
                return false;
            }
            else{
                this.setSource(getMyDao().queryForId(id));
                return true;
            }
        } catch (Exception db) {
            source.setId(oldId);
            this.handleError("The getEntity operation in the class: " + source.getClass().getCanonicalName() + " was impossible for the value: " + (C) source.getId());
            db.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a boolean.
     * Updates an object instance into the database, referenced by the embedded object's key.
     *
     * @return true if the instance is updated.
     */
    public boolean update(T entity) {
        try {
            setMyDao(getMyDao());
            if(getMyDao().update(entity)==1) return true;
            return false;
        } catch (Exception db) {
            this.handleError("The update operation in the class: " + source.getClass().getCanonicalName() + " was impossible for the given entity");
            return false;
        }
    }

    /**
     * Returns a boolean.
     * Updates an object instance into the database, referenced by entity's key.
     *
     * @param entity the instance to be updated.
     * @return true if the instance is updated.
     */
    public boolean update() {
        try {
            setMyDao(getMyDao());
            getMyDao().update(this.getSource());
            return true;
        } catch (Exception db) {
            this.handleError("The update operation in the class: " + source.getClass().getCanonicalName() + " was impossible for the value: " + source.getId());
            return false;
        }
    }

    /**
     * Returns a boolean.
     * Search an object instance in the database and then deletes it.
     *
     * @param a the ID of the object to be deleted.
     * @return true if the instance is successfully deleted.
     */
    public boolean findAndDelete(C a) {
        try {
            if (this.find(a)) {
                this.delete(this.getSource());
            }
            return true;
        } catch (Exception db) {
            this.handleError("The getEntity and deletion operation in the class: " + source.getClass().getCanonicalName() + " was impossible for the value: " + source.getId());
            return false;
        }
    }

    /**
     * Creates or updates an object, depending if it's in the database or not.
     * @param a The entity to be persisted
     * @return true if the entity was persisted, false if either create or update were impossible.
     */
    public boolean persist(T a) {
        try {
            if (this.find((C)a.getId())) {
               return this.update(a);
            }
            else{
                return this.create(a);
            }
        } catch (Exception db) {
            this.handleError("The getEntity and create/update operation in the class: " + source.getClass().getCanonicalName() + " was impossible for the value: " + source.getId());
            System.out.println("PERSIST FAIL");
            db.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a boolean.
     * Deletes an object instance from the database.
     *
     * @param entity the object to be deleted.
     * @return true if the instance is successfully deleted.
     */
    public boolean delete(T entity) {
        try {
            setMyDao(getMyDao());
            getMyDao().delete(entity);
            return true;
        } catch (Exception db) {
            this.handleError("The deletion operation in the class: " + source.getClass().getCanonicalName() + " was impossible for the value: " + (C) source.getId());
            return false;
        }
    }

    /**
     * If possible, creates instances of every object in the list into the database. If any object exists, then updates it instead.
     *
     * @param list the objects to be instantiated.
     */
    public void importList(List<T> list) {
        Container objectFactory = new Container(this.source);
        T n = null;
        try {
            n = (T) objectFactory.createContents();
            n = this.getSource();

            for (T s : list) {
                this.setSource(s);
                if (find()) {
                    this.update();
                } else {
                    this.create();
                }
            }
            this.setSource(n);
        } catch (IllegalAccessException | InstantiationException db) {
            this.handleError("objectFactory could not instantiate the " + source.getClass().getCanonicalName() + " class");
        }
    }

    /*Commented: not functional yet. Waiting ot get a POJO mapper.
    public void importObjectList(List<Object> list){
        for (Object s : list) {
            try {
                this.createFromObject(s);
            } catch (Exception dbxists) {
                //if found, update.
                try {
                    this.updateFromObject(s);
                } catch (Exception update) {
                    //if update fails, manage error message.
                }
            }
        }
    }
    */

    /**
     * Returns all the objects from the database, either in FIFO or LIFO order.
     *
     * @param invertOrder * @param invertOrder indicates if the list must be LIFO or FIFO ordered. True for LIFO, false for FIFO (default order).
     * @return a list with the table contents in the desired order.
     */
    public List<T> getAll(boolean invertOrder) {

        List<T> list = new ArrayList<T>();
        try {
            setMyDao(getMyDao());
            list = (List<T>) getMyDao().queryForAll();
        } catch (Exception rescueList) {
            this.handleError("It was impossible to get the " + source.getClass().getCanonicalName() + "'s list");
        }
        if (invertOrder) {
            java.util.Collections.reverse(list);
        }
        return list;
    }

    /**
     * Returns all the objects from the database which complains with column=value, either in FIFO or LIFO order.
     *
     * @param column      the column that will be used to filter the group.
     * @param value       the value that the column must have.
     * @param invertOrder indicates if the list must be LIFO or FIFO ordered. True for LIFO, false for FIFO (default order).
     * @return A list with all the complaining instances.
     */
    public List<T> getGroup(String column, Object value, boolean invertOrder) {
        List<T> list = new ArrayList<T>();
        try {
            setMyDao(getMyDao());
            list = getMyDao().queryForEq(column, value);
        } catch (Exception db) {
            this.handleError("It was impossible to get the " + source.getClass().getCanonicalName() + "'s list");
        }
        if (invertOrder) {
            java.util.Collections.reverse(list);
        }
        return list;
    }

    /**
     * Returns all the objects from the database matching all the values into @map, either in FIFO or LIFO order.
     *
     * @param map         a Map containing a String for identifying the column to be used, and the value to be searched.
     * @param invertOrder indicates if the list must be LIFO or FIFO ordered. True for LIFO, false for FIFO (default order).
     * @return a list with all the complaining instances.
     */
    public List<T> getGroup(Map map, boolean invertOrder) {
        List<T> list = new ArrayList<T>();
        try {
            setMyDao(getMyDao());
            list = getMyDao().queryForFieldValues(map);
        } catch (Exception rescueList) {
            this.handleError("It was impossible to get the " + source.getClass().getCanonicalName() + "'s group");
            rescueList.printStackTrace();
        }
        if (invertOrder) {
            java.util.Collections.reverse(list);
        }

        return list;
    }

    private Where getBetweenArgument(String column, Object lowValue, Object topValue,
                                     boolean withID, Where where) throws SQLException {
        if (withID) {
            where.eq(this.routeToId, (C) source.getId());
            where.and();
        }
        where.between(column, lowValue, topValue);
        return where;
    }

    private Where getSimpleMapArguments(Map<String, Object> map, Where where) throws SQLException {

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            where.and();
            where.eq(key, value);
        }
        return where;
    }

    /**
     * Returns all the objects from the database that matches the condition "column between lowValue and topValue".
     * Optionally, it allows to search by primary key, and several more fields using a map.
     *
     * @param column   column to be evaluated
     * @param lowValue lowest value
     * @param topValue highest value
     * @param withID   true to use the ID of the source object embedded in the DAO. False to not use it.
     * @param map      Column, Value. Allows to create more search parameters.
     */
    public List<T> getBetween(String column, Object lowValue, Object topValue,
                              boolean withID, Map<String, Object> map) {

        List<T> list = new ArrayList<T>();
        try {
            setMyDao(getMyDao());
            QueryBuilder<T, C> qb = getMyDao().queryBuilder();
            Where where = qb.where();
            where = this.getSimpleMapArguments(map, this.getBetweenArgument(column, lowValue, topValue,
                    withID, where));
            PreparedQuery<T> preparedQuery;
            preparedQuery = qb.prepare();
            list = getMyDao().query(preparedQuery);
            return list = getMyDao().query(preparedQuery);
        } catch (SQLException db) {
            // TODO Auto-generated catch block
            this.handleError("Query error: " + source.getClass().getCanonicalName() + "the query was malformed " + db.getStackTrace());
            return list;
        }
    }

    /**
     * Returns all the objects from the database that matches the condition "column between lowValue and topValue".
     * Optionally, it allows to search by primary key.
     *
     * @param column   column to be evaluated
     * @param lowValue lowest value
     * @param topValue highest value
     * @param withID   true to use the ID of the source object embedded in the DAO. False to not use it.
     */
    public List<T> getBetween(String column, Object lowValue, Object topValue,
                              boolean withID) {

        List<T> list = new ArrayList<T>();
        try {
            setMyDao(getMyDao());
            QueryBuilder<T, C> qb = getMyDao().queryBuilder();
            Where where = qb.where();
            where = this.getBetweenArgument(column, lowValue, topValue, withID, where);
            PreparedQuery<T> preparedQuery;
            preparedQuery = qb.prepare();
            list = getMyDao().query(preparedQuery);
            return list;
        } catch (SQLException db) {
            // TODO Auto-generated catch block
            this.handleError("Query error: " + source.getClass().getCanonicalName() + "the query was malformed " + db.getStackTrace());
            return list;
        }
    }

    /**
     * Creates a LinkedHashMap of type <C, T> from a list of type <T>. Allows to search through the received entities
     * using the ID field without iterating. Supports regular iteration too. Useful for collections that will be used
     * in any selector-like control.
     *
     * @param list a List<T> where T is the entity type.
     * @return a LinkedHashMap<Entity.IdField, Entity>
     */
    public Map<C, T> getDataAsMap(List<T> list) {
        Map<C, T> map = new LinkedHashMap<>();
        for (T element : list) {
            map.put((C) element.getId(), element);
        }
        return map;
    }

    /**
     * Returns all the objects from the database matching all the conditions in the map, either in FIFO or LIFO order.
     * Optionally, it can use the embedded object's primary key to search as well.
     *
     * @param withID  true to use the ID of embedded entity (source)
     * @param map     the query's parameters. "String" is an identifier for the whole argument. It should
     *                be table field+number and it allows to modify the query a lot faster than a list,
     *                while Object[] contains the method to be executed, the value to be searched and finally
     *                the table field to be evaluated. The map must be an instance or subclass of LinkedHashMap if you
     *                want to use the OR clause.
     * @param reverse true for LIFO, false for FIFO.
     * @return a list with the complaining elements.
     */
    public List<T> getListByQuery(boolean withID, boolean reverse, Map<String, Object[]> map) {

        List<T> list = new ArrayList<T>();

        try {
            setMyDao(getMyDao());
            QueryBuilder<T, C> qb = getMyDao().queryBuilder();
            Where where = qb.where();
            if (withID) {
                where.eq(this.routeToId, (C) source.getId());
                where.and();
            }
            boolean previousWhere = false;

            for (Map.Entry<String, Object[]> entry : map.entrySet()) {
                String keyA = entry.getKey();
                Object[] value = entry.getValue();
                String key = value[2].toString();

                if (value[0].toString().equalsIgnoreCase("or") || value[0].toString().equalsIgnoreCase("||")) {
                    where.or();
                    previousWhere = false;
                }
                if (previousWhere) {
                    where.and();
                    previousWhere = false;
                }

                if (value[0].toString().equalsIgnoreCase("like")) {
                    if (!value[1].toString().contains("%")) {
                        value[1] = value[1].toString() + "%";
                    }
                    where.like(key, value[1]);
                    previousWhere = true;
                }
                if (value[0].toString().equalsIgnoreCase("IN")) {
                    where.in(key, value[1]);
                    previousWhere = true;
                }
                if (value[0].toString().equalsIgnoreCase("notIn")) {
                    where.notIn(key, value[1]);
                    previousWhere = true;
                }
                if (value[0].toString().equalsIgnoreCase(">=")) {
                    where.ge(key, value[1]);
                    previousWhere = true;
                }
                if (value[0].toString().equalsIgnoreCase(">")) {
                    where.gt(key, value[1]);
                    previousWhere = true;
                }
                if (value[0].toString().equalsIgnoreCase("IS NULL")) {
                    where.isNull(key);
                    previousWhere = true;
                }
                if (value[0].toString().equalsIgnoreCase("NOT NULL")) {
                    where.isNotNull(key);
                    previousWhere = true;
                }
                if (value[0].toString().equalsIgnoreCase("<=")) {
                    where.le(key, value[1]);
                    previousWhere = true;
                }
                if (value[0].toString().equalsIgnoreCase("<")) {
                    where.lt(key, value[1]);
                    previousWhere = true;
                }
                if (value[0].toString().equalsIgnoreCase("!=")) {
                    where.ne(key, value[1]);
                    previousWhere = true;
                }
                if (value[0].toString().equalsIgnoreCase("=")) {
                    where.eq(key, value[1]);
                    previousWhere = true;
                }
                if (value[0].toString().equalsIgnoreCase("NOT")) {
                    where.not();
                    previousWhere = true;
                }
            }

            qb.setWhere(where);
            PreparedQuery<T> preparedQuery;
            preparedQuery = qb.prepare();
            list = getMyDao().query(preparedQuery);

        } catch (SQLException db) {
            // TODO Auto-generated catch block
            this.handleError("Query error: " + source.getClass().getCanonicalName() + "the query was malformed " + db.getStackTrace());
            return list;
        }

        if (reverse) {
            java.util.Collections.reverse(list);
        }
        return list;
    }

    /*
    Performs a IN search
     */
    public List<T> getListWhereIn(boolean withID, boolean reverse, String column, List<Object> values) {

        List<T> list = new ArrayList<T>();

        try {
            setMyDao(getMyDao());
            QueryBuilder<T, C> qb = getMyDao().queryBuilder();
            Where where = qb.where();
            if (withID) {
                where.eq(this.routeToId, (C) source.getId());
                where.and();
            }
            where.in(column, values);

            qb.setWhere(where);
            PreparedQuery<T> preparedQuery;
            preparedQuery = qb.prepare();
            list = getMyDao().query(preparedQuery);

        } catch (SQLException db) {
            // TODO Auto-generated catch block
            this.handleError("Query error: " + source.getClass().getCanonicalName() + "the query was malformed " + db.getStackTrace());
            db.printStackTrace();
            return list;
        }

        if (reverse) {
            java.util.Collections.reverse(list);
        }
        return list;
    }

}
