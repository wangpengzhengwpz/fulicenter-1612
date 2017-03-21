package cn.ucai.fulicenter.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/3/21.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String FULTCENTER_USER_TABLE_CREATE =
            "CREATE TABLE " + UserDao.USER_TABLE_NAME + " ("
                    + UserDao.USER_COLUMN_NAME + " TEXT PRIMARY KEY,"
                    + UserDao.USER_COLUMN_NICK + " TEXT,"
                    + UserDao.USER_COLUMN_AVATAR + " INTEGER,"
                    + UserDao.USER_COLUMN_AVATAR_PATH + " TEXT,"
                    + UserDao.USER_COLUMN_AVATAR_TYPE + " INTEGER,"
                    + UserDao.USER_COLUMN_AVATAR_SUFFIX + " TEXT,"
                    + UserDao.USER_COLUMN_AVATAR_UPDATE_TIME + " TEXT);";
    private static final int DATABASE_VERSION = 1;
    private static DBOpenHelper instance;

    public synchronized static DBOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBOpenHelper(context);
        }
        return instance;
    }

    public DBOpenHelper(Context context) {
        super(context, getDatabaseNames(), null, DATABASE_VERSION);
    }

    public static String getDatabaseNames() {
        return "cn.ucai.fulicenter.db";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(FULTCENTER_USER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public void closeDB() {
        if (instance != null) {
            instance.close();
        }
        instance = null;
    }
}
