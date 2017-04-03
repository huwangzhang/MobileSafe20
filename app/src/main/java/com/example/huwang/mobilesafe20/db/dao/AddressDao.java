package com.example.huwang.mobilesafe20.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.huwang.mobilesafe20.utils.FormatUtils;

/**
 * Created by huwang on 2017/4/3.
 */

public class AddressDao {
    private Context context;

    public AddressDao(Context context) {
        super();
        this.context = context;
    }

    /**
     * 查询归属地
     * @param number
     * @return
     */
    public String findAddressByNumber(String number) {
        String path = "data/data/" + context.getPackageName() + "/databases/address.db";
        String address = "未知 ";
        if (FormatUtils.isPhone(number)) {
            address = findMobilePhone(number, path, address);
        } else {
            // 固话
            // select location from data2 where area ='20'; -- 取得运营商前面的地址
            // 010 8
            if (number.length() == 11) {
                // 北京广广州
                String sql = " select location from data2 where area=?;";
                String area = number.substring(1, 3);
                address = findPhone(path, address, sql, area);
            } else if (number.length() == 12) {
                // 二线
                // 0459 23232323
                //
                // select location from data2 where area='459'; -- 取得运营商前面的地址
                String sql = " select location from data2 where area=?;";
                String area = number.substring(1, 4);
                address = findPhone(path, address, sql, area);
            }
        }

        return address;
    }

    private String findPhone(String path, String address, String sql, String area) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(sql, new String[] { area });
        if (cursor.moveToNext()) {
            address = cursor.getString(cursor.getColumnIndex("location"));
            // 去掉联通 移动 电信
            address = address.replace("联通", "");
            address = address.replace("移动", "");
            address = address.replace("电信", "");
        }
        cursor.close();
        db.close();
        return address;
    }

    /**
     * 查询固话
     * @param number
     * @param path
     * @param address
     * @return
     */
    private String findMobilePhone(String number, String path, String address) {
        //
        // 手机
        // 18701606281
        // 1870160
        // 1300001 七位
        // --out key
        String sql = " select location from data2 where id=(select outkey from data1  where id=?);";
        // --根据outkey 查找 lcoation
        // 获取数据库实例 ：使用外部数据库 导入 使用静态打开
        // SQLiteDatabase db=SQLiteDatabase.openDatabase(路径 , 游标 工厂 null 默认,
        // 方式 读/写);
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(sql, new String[] { number.substring(0, 7) });// substring
        // start
        // 包含
        // end
        // 不包含

        if (cursor.moveToNext()) {
            address = cursor.getString(cursor.getColumnIndex("location"));
        }
        cursor.close();
        // 关闭
        db.close();
        return address;
    }
}
