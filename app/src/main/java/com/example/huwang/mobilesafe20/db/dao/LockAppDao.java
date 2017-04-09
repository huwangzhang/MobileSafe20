package com.example.huwang.mobilesafe20.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwang on 2017/4/8.
 */

public class LockAppDao {
    private Context context;
    private MyHelper mMyHelper;

    public LockAppDao(Context context) {
        super();
        this.context = context;
        mMyHelper = new MyHelper(context);
    }

    private class MyHelper extends SQLiteOpenHelper {

        public MyHelper(Context context) {
            super(context, "applock.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // 创建表
            // create table lockapps (id integer primary key
            // autoincrement,packagename text );
            //
            Log.i("zhang", "建表成功");
            String sql = "create table  lockapps    (id integer primary key autoincrement,packagename  text );";
            db.execSQL(sql);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }

    }

    // 添加--加锁
    public void add(String packname) {
        SQLiteDatabase db = mMyHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packagename", packname);
        db.insert("lockapps", "", values);
        db.close();

        //发出信号
        Uri uri=Uri.parse("content://"+LockAppDao.class.getName());
        //表明数据库表被修改  引起数据变化
//		context.getContentResolver().notifyChange(访问地址：表示信号, 内容观察者 null 通知所有的);
        context.getContentResolver().notifyChange(uri, null);
    }

    //
    // 删除 -- 解锁
    // delete from lockapps where packagename='';
    //
    public void delete(String packagename) {

        SQLiteDatabase db = mMyHelper.getWritableDatabase();
        db.delete("lockapps", "packagename=?", new String[] { packagename });
        db.close();
        //发出信号
        Uri uri=Uri.parse("content://"+LockAppDao.class.getName());
        //表明数据库表被修改  引起数据变化
//		context.getContentResolver().notifyChange(访问地址：表示信号, 内容观察者 null 通知所有的);
        context.getContentResolver().notifyChange(uri, null);
    }

    // 查找 --是否加锁
    public boolean isLock(String packagename) {
        boolean flag = false;
        String sql = "select id from lockapps where packagename=?;";
        SQLiteDatabase db = mMyHelper.getWritableDatabase();
        Cursor c = db.rawQuery(sql, new String[] { packagename });
        if (c.getCount() > 0) {
            flag = true;
        }
        c.close();
        db.close();
        return flag;
    }
    // 查找所有
    // select packagename from lockapps ;
    public List<String> findAllLockApps() {
        List<String> apps=new ArrayList<String>();
        String sql = "select packagename from lockapps ;";
        SQLiteDatabase db = mMyHelper.getWritableDatabase();
        Cursor c = db.rawQuery(sql, new String[] {  });

        while(c.moveToNext())
        {
            String packagename=c.getString(c.getColumnIndex("packagename"));
            apps.add(packagename);

        }
        c.close();
        db.close();
        return apps;
    }
}
