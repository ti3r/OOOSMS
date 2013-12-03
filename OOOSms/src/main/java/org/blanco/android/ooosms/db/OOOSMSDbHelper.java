package org.blanco.android.ooosms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;

import java.sql.SQLException;

/**
 * Created by Alexandro Blanco <ti3r.bubblenet@gmail.com> on 11/29/13.
 */
public class OOOSMSDbHelper extends OrmLiteSqliteOpenHelper {

    public static final int OOSMSDB_HEAD_VERSION = 1;

    public OOOSMSDbHelper(Context context, int databaseVersion) {
        super(context, "OOSMSDB", null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            DatabaseConnection connection = connectionSource.getReadWriteConnection();
            connection.setAutoCommit(true);
            connection.executeStatement("Create Table ListEntries(_id numeric, phone text, message text, rowOrder numeric)",0);

        } catch (SQLException e) {
            throw new IllegalStateException("Unable to create tables for OOSMSDbHelper");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL("Create Table ListEntries(_id numeric, phone text, message text, rowOrder numeric)");
        sqLiteDatabase.endTransaction();
    }
}
