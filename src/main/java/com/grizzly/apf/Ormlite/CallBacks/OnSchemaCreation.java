package com.grizzly.apf.Ormlite.CallBacks;

import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by fpardo on 12/10/14.
 */
public interface OnSchemaCreation {
    void createTables(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) throws SQLException;
}
