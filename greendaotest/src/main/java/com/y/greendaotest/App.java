package com.y.greendaotest;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.y.greendaotest.gen.DaoMaster;
import com.y.greendaotest.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * @author yu
 *         Create on 16/9/3.
 */
public class App extends Application {

    // 建立全局的daoSession,防止多次创建数据库连接
    public static DaoSession daoSession;
    public static SQLiteDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
    }

    private void setupDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        // 数据库的名字是 my_db
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "my_db", null);
        db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值,也可以不设
        QueryBuilder.LOG_SQL = BuildConfig.DEBUG;
        QueryBuilder.LOG_VALUES = BuildConfig.DEBUG;
    }
}
