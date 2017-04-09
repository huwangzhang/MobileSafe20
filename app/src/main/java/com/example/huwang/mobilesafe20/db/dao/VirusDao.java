package com.example.huwang.mobilesafe20.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * Created by huwang on 2017/4/6.
 */

public class VirusDao {
    /**
     * 获取病毒描述
     *
     * @param md5
     * @return
     */
    public static String getVirusDesc(String md5) {
        String desc = null;
        // 外部数据库
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/antivirus.db";
//        Log.i("zhang", path);
        // 获取数据库实例
        // SQLiteDatabase db=SQLiteDatabase.openDatabase(路径, 游标工厂, 读写方式);
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        // select desc from datable where
        // md5='a2bd62c99207956348986bf1357dea01';
        String sql = "select desc from datable where  md5=?;";

        Cursor c = db.rawQuery(sql, new String[] { md5 });

        if (c.moveToNext()) {
            desc = c.getString(c.getColumnIndex("desc"));
        }
        // 关闭
        c.close();
        db.close();
        return desc;
    }

    // 更新版本号 update version set subcnt=1722;

    public static String updateVersion(String versionName) {
        String desc = null;
        // 外部数据库
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/antivirus.db";

        // 获取数据库实例
        // SQLiteDatabase db=SQLiteDatabase.openDatabase(路径, 游标工厂, 读写方式);
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        String sql = " update version set subcnt=?;";
        db.execSQL(sql, new String[]{versionName});
        db.close();
        return desc;
    }
    // 同步新病毒特征 insert into datable (md5,type,desc)values('61352bc6c71e85a0137c3382f1215ff6',6,'av强大影响学习的病毒');
    public static String addMd5(String md5,String type,String desc) {
        // 外部数据库
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/antivirus.db";
        // 获取数据库实例
        // SQLiteDatabase db=SQLiteDatabase.openDatabase(路径, 游标工厂, 读写方式);
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        String sql="insert into datable (md5,type,name,desc)values(?,?,?,?);";
        db.execSQL(sql, new String[]{md5,7+"","av病毒",desc});
        db.close();
        return desc;
    }
}
