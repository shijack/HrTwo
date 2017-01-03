package com.zerostudio.hrtwo;

import android.app.Application;
import android.util.Log;

import com.zerostudio.hrtwo.db.dao.bean.QuesstionInfo;
import com.zerostudio.hrtwo.db.dao.bean.QuesstionInfoDao;

import net.sqlcipher.database.SQLiteDatabase;


/**
 * Created by huangzj on 2015/12/21.
 */
public class MyApplication extends Application {

    public static final String TAG = "MyApplication";
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "3");
        instance = this;

        initDb();

//        testCity();
    }

    private void initDb() {
        SQLiteDatabase.loadLibs(this);
        // TODO: 2017/1/3 将数据库加密。上线之前讲assets文件中的分割文件变为加密的分割文件。

//        DatabaseEncrypted.importUnencryptedDatabase(
//                this,
//                DatabaseHelper.DATABASE_NAME,
//                DatabaseHelper.ENCRYPTED_DATABASE_NAME,
//                DatabaseHelper.PASSWORD);

//        DatabaseEncrypted.exportToUnencryptedDatabase(this,
//                DatabaseHelper.ENCRYPTED_DATABASE_NAME,
//                DatabaseHelper.DATABASE_NAME,
//                DatabaseHelper.PASSWORD);
    }

    private void testCity() {
        QuesstionInfo quesstionInfo = new QuesstionInfo();
        quesstionInfo.setANSWER("A");
        quesstionInfo.setTITLE("在生产要素市场，（   ）是生产要素的供给者。");
        quesstionInfo.setEXPLAIN("（基础P2）在生产要素市场，居民户是生产要素的供给者（A对），企业是生产要素的需求者。企业由于生产要素的使用须向居民户支付要素报酬（如工资）。");
        new QuesstionInfoDao(getContext(),QuesstionInfo.class).insert(quesstionInfo);

    }

    public static MyApplication getContext() {
        return instance;
    }
}
