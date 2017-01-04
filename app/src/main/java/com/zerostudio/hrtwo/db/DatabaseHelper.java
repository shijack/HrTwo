package com.zerostudio.hrtwo.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.JavaxPersistence;
import com.j256.ormlite.sqlcipher.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import com.zerostudio.hrtwo.db.dao.bean.QuesstionInfo;
import com.zerostudio.hrtwo.db.dao.bean.QuesstionOption;
import com.zerostudio.hrtwo.db.dao.bean.Section;

import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private String TAG = "DatabaseHelper";

    public static final String DATABASE_NAME = "humanresource_second_tiku.db";
    public static final String ENCRYPTED_DATABASE_NAME = "encrypted";
    public static final String PASSWORD = "DB-way2-password";


    /**
     * 数据库版本号
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * dao缓存
     */
    private Map<String, Dao> daos = new HashMap<String, Dao>();

    /**
     * 全局单例
     */
    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME,PASSWORD, null, DATABASE_VERSION);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        createTables(database, connectionSource);
    }

    /**
     * 数据库升级，注意控制好数据库版本号，不然此方法将不会被调用到
     */
    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, QuesstionInfo.class, true);
            TableUtils.dropTable(connectionSource, QuesstionOption.class, true);
            TableUtils.dropTable(connectionSource, Section.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        if (isTableExist(sqLiteDatabase, QuesstionInfo.class) || isTableExist(sqLiteDatabase, QuesstionOption.class) || isTableExist(sqLiteDatabase, Section.class)) {
            Log.d(TAG, "所有数据表已经存在了");
            return;
        }
        try {
            TableUtils.createTable(connectionSource, QuesstionInfo.class);
            TableUtils.createTable(connectionSource, QuesstionOption.class);
            TableUtils.createTable(connectionSource, Section.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public synchronized Dao getDao(Class clazz) {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }else{
            try {
                dao = super.getDao(clazz);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        synchronized (this) {
            Iterator<Map.Entry<String, Dao>> it = daos.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Dao> entry = it.next();
                Dao dao = entry.getValue();
                dao = null;
                it.remove();
            }
        }
    }

    private <T> boolean isTableExist(SQLiteDatabase database, Class<T> clazz) {
        boolean isTableExist = false;
        String tableName = extractTableName(clazz);
        Cursor cursor = database.rawQuery("select * from sqlite_master where type = ? AND name = ?",
                new String[]{"table", tableName});
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("sql");
            if (-1 != columnIndex && cursor.getCount() > 0) {
                isTableExist = true;
            }
            cursor.close();
        }
        return isTableExist;
    }

    /**
     * 获取表名
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> String extractTableName(Class<T> clazz) {
        DatabaseTable databaseTable = clazz.getAnnotation(DatabaseTable.class);
        String name;
        if (databaseTable != null && databaseTable.tableName() != null && databaseTable.tableName().length() > 0) {
            name = databaseTable.tableName();
        } else {
            name = JavaxPersistence.getEntityName(clazz);
            if (name == null) {
                name = clazz.getSimpleName().toLowerCase();
            }
        }
        return name;
    }
}
