package com.zerostudio.hrtwo;

import android.app.Application;
import android.util.Log;

import com.zerostudio.hrtwo.db.dao.bean.Article;
import com.zerostudio.hrtwo.db.dao.bean.User;
import com.zerostudio.hrtwo.db.dao.ormlite.ArticleDao;
import com.zerostudio.hrtwo.db.dao.ormlite.UserDao;

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

        testCity();
    }

    private void initDb() {
        SQLiteDatabase.loadLibs(this);

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
            User u = new User();
            u.setName("张鸿洋");
            new UserDao(getContext()).add(u);
            Article article = new Article();
            article.setTitle("ORMLite的使用");
            article.setUser(u);
            new ArticleDao(getContext()).add(article);

    }

    public static MyApplication getContext() {
        return instance;
    }
}
