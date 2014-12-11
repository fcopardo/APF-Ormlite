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
import android.database.sqlite.SQLiteDatabase;
import com.grizzly.apf.Ormlite.CallBacks.OnConnectionClose;
import com.grizzly.apf.Ormlite.CallBacks.OnSchemaCreation;
import com.grizzly.apf.Ormlite.CallBacks.OnSchemaUpdate;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * Ormlite manager, manages the database connection and provides the database schema.
 */
public class ORMSchema extends OrmLiteSqliteOpenHelper {

    /**
     * Class members
     * Callbacks for database creation, update, and connection closing.
     */
    private OnSchemaCreation schemaCreator = null;
    private OnSchemaUpdate schemaUpdater = null;
    private OnConnectionClose connectionCloser = null;


    public ORMSchema(Context context, String database, int version) {
        super(context, database, null, version);
    }

    public ORMSchema(Context context) {
        super(context, DefinitionsDatabase.getDatabaseName(), null, DefinitionsDatabase.getDatabaseVersion());
    }

    public ORMSchema(Context context, String databaseName,
                     CursorFactory factory, int databaseVersion) {

        super(context, databaseName, factory, databaseVersion);
        // TODO Auto-generated constructor stub
    }

    public void setSchemaCreator(OnSchemaCreation schemaCreator) {
        this.schemaCreator = schemaCreator;
    }

    public void setSchemaUpdater(OnSchemaUpdate schemaUpdater) {
        this.schemaUpdater = schemaUpdater;
    }

    public void setConnectionCloser(OnConnectionClose connectionCloser) {
        this.connectionCloser = connectionCloser;
    }

    /**
     * Creates the database, if not found, in the connection specified in connectionSource.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

        try {
            if(schemaCreator != null){
                schemaCreator.createTables(sqLiteDatabase, connectionSource);
            }
            /**
             * Add all the entities to be created here.
             * Example:
             * TableUtils.createTable(connectionSource, entityClass.class);
             */

        } catch (Exception e) {
            /**
             * It should be an SQLException, but since there is no code to fire the exception is not possible to use it.
             * UPDATE: Now it can be an SQL Exception.
             */
            throw new RuntimeException(e);
        }
    }

    /**
     * If the database in connectionSource requires it (oldVersion<newVersion), upgrades it.
     */
    @Override
    public void onUpgrade(final SQLiteDatabase db, final ConnectionSource connectionSource, int oldVersion, final int newVersion) {
        db.beginTransaction();
        try {
            if(schemaUpdater != null){
                schemaUpdater.updateTables(db, connectionSource, oldVersion, newVersion);
            }
            /**
             * Add all the update statements here.
             * Example:
             * single column:
             * db.execSQL("ALTER TABLE `table` ADD COLUMN column DATATYPE;");
             * Full table creation:
             * TableUtils.createTable(connectionSource, entityClass.class);
             * Full table deletion:
             * TableUtils.dropTable(connectionSource, entityClass.class);
             */
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void close() {
        super.close();
        if(connectionCloser!=null){
            connectionCloser.closeConnection();
        }

        /**
         * Set all DAOs to null to close the database.
         * Example:
         * setEntityDao(null);
         */
    }
}
