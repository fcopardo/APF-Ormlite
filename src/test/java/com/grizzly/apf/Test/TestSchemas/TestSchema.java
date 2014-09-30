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
package com.grizzly.apf.Test.TestSchemas;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.grizzly.apf.Ormlite.DefinitionsDatabase;
import com.grizzly.apf.Test.TestEntities.PersonTest;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import com.j256.ormlite.table.TableUtils;

/**
 * Ormlite manager, manages the database connection and provides the database schema.
 */
public class TestSchema extends OrmLiteSqliteOpenHelper {


    public TestSchema(Context context, String database, int version) {
        super(context, database, null, version);
    }

    public TestSchema(Context context) {
        super(context, DefinitionsDatabase.getDatabaseName(), null, DefinitionsDatabase.getDatabaseVersion());
    }

    public TestSchema(Context context, String databaseName,
                      CursorFactory factory, int databaseVersion) {

        super(context, databaseName, factory, databaseVersion);
        // TODO Auto-generated constructor stub
    }

    /**
     * Creates the database, if not found, in the connection specified in connectionSource.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

        try {
            /**
             * Add all the entities to be created here.
             * Example:
             * TableUtils.createTable(connectionSource, entityClass.class);
             */
        TableUtils.createTable(connectionSource, PersonTest.class);

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

        /**
         * Set all DAOs to null to close the database.
         * Example:
         * setEntityDao(null);
         */
    }
}
