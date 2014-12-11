package com.grizzly.apf.Ormlite.CallBacks;

import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Created by fpardo on 12/10/14.
 */
public interface OnSchemaUpdate {
    void updateTables(final SQLiteDatabase db, final ConnectionSource connectionSource, int oldVersion, final int newVersion);
}
