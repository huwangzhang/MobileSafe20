package com.example.huwang.mobilesafe20.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.huwang.mobilesafe20.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwang on 2017/3/30.
 */

public class BlackNumberDao {
    // Dao访问数据库的对象 ，提供增删改查方法
    private class MyHelper extends SQLiteOpenHelper {
        public MyHelper(Context context) {
            // super(上下文, 数据库名, 游标工厂 默认为null, 版本号);
            super(context, "numbers.db", null, 1);
        }

        // 初始化数据库
        @Override
        public void onCreate(SQLiteDatabase db) {
            // 建表语句
            String sql = "create table numbers (id integer primary key autoincrement ,number text,mode text);";
            db.execSQL(sql);// 发送命令
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

    // sqlite数据库是一个弱类型数据库
    private MyHelper mMyHelper = null;
    private Context context = null;

    public BlackNumberDao(Context context) {
        super();
        this.context = context;
        this.mMyHelper = new MyHelper(context);
    }

    // 查询
    // select * from numbers order by id desc;

    /**
     * 查询
     *
     * @return
     */
    @Deprecated
    public List<BlackNumberInfo> findAll() {
        List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
        // 获取实例
        SQLiteDatabase db = mMyHelper.getWritableDatabase();
        String sql = "select * from numbers order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[] {});
        cursorToList(list, cursor);
        cursor.close();
        // 关闭数据库
        db.close();
        return list;
    }

    /**
     * 根据号码获取模式
     * @param number
     * @return
     */
    public String findModeByNumber(String number) {
        String mode = "";
        // 获取实例
        SQLiteDatabase db = mMyHelper.getWritableDatabase();

        // select mode from numbers where number='10010';
        String sql = "select mode from numbers where  number=?;";
        Cursor cursor = db.rawQuery(sql, new String[] { number });

        if (cursor.moveToNext())// 默认指向位置不是第一行数据
        {
            mode = cursor.getString(cursor.getColumnIndex("mode"));
        }
        // 0 2
        cursor.close();
        // 关闭数据库
        db.close();
        return mode;
    }

    /**
     * 查询分页
     *
     * @param currpage
     * @param maxEachPage
     * @return
     */
    public List<BlackNumberInfo> findPageByIndex(int currpage, int maxEachPage) {
        List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
        // 获取实例
        SQLiteDatabase db = mMyHelper.getWritableDatabase();
        // 第一页 1 20 0-19
        // 第二页 2 20 20-39
        // 第三页 3 20 40-59
        // 第N页 (currpage-1)*maxEachPage currpage*maxEachPage-1
        // limit 限制返回个数 offset 开始下标
        // select * from numbers limit 20 offset 40;
        int positionStart = (currpage - 1) * maxEachPage;
        String sql = "select * from numbers   limit ? offset ?;";
        Cursor cursor = db.rawQuery(sql, new String[] { maxEachPage + "", positionStart + "" });
        cursorToList(list, cursor);
        cursor.close();
        // 关闭数据库
        db.close();
        return list;
    }

    /**
     * 游标转换成集合
     *
     * @param list
     * @param cursor
     */
    private void cursorToList(List<BlackNumberInfo> list, Cursor cursor) {
        while (cursor.moveToNext()) {
            String number = cursor.getString(cursor.getColumnIndex("number"));
            String mode = cursor.getString(cursor.getColumnIndex("mode"));
            String id = cursor.getString(cursor.getColumnIndex("id"));

            BlackNumberInfo info = new BlackNumberInfo();
            info.id = id;
            info.mode = mode;
            info.number = number;

            list.add(info);
        }
    }

    //
    /**
     * 添加
     *
     * @param info
     * @return
     */
    public BlackNumberInfo add(BlackNumberInfo info) {
        // 获取实例
        SQLiteDatabase db = mMyHelper.getWritableDatabase();
        // 比较底层
        // String sql = "insert into numbers (number,mode)values(?,?);";// 使用占位符
        // db.execSQL(sql, new String[] { info.number, info.mode });
        // ContentValues:map集合 代表一条插入记录
        ContentValues values = new ContentValues();
        values.put("number", info.number);
        values.put("mode", info.mode);
        long id = db.insert("numbers", "", values);
        info.id = id + "";
        // 关闭数据库
        db.close();
        return info;
    }

    //
    // 删除
    public void delete(String id) {
        // 获取实例
        SQLiteDatabase db = mMyHelper.getWritableDatabase();
        // 比较底层
        // delete from numbers where id=1;
        db.delete("numbers", " id=?", new String[] { id });
        // 关闭数据库
        db.close();
    }

    //
    // 修改
    public void update(BlackNumberInfo info) {
        // 获取实例
        SQLiteDatabase db = mMyHelper.getWritableDatabase();
        // 比较底层
        // update numbers set mode='1' where id=2;
        ContentValues values = new ContentValues();
        values.put("mode", info.mode);
        db.update("numbers", values, "id=?", new String[] { info.id });
        // 关闭数据库
        db.close();
    }
}
