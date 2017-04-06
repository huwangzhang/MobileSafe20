package com.example.huwang.mobilesafe20.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by huwang on 2017/3/19.
 */

public class ToastUtil {
    public static void shortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static View showInWindow(Context context, int resid) {
        //视图
        View view = View.inflate(context, resid, null);
        //获取管理者
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;//背景透明
        params.gravity = Gravity.TOP | Gravity.LEFT;
//        params.type = WindowManager.LayoutParams.TYPE_TOAST;//类型 显示的级别
        params.type = WindowManager.LayoutParams.TYPE_TOAST;//类型 显示的级别
        params.x = 140;
        params.y = 250;//绝对布局
//        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON  //高亮
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  //不能聚焦
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE; //不能拖动
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; //高亮
        wm.addView(view, params);
        return view;
    }

    public static void dismissInWindow(Context context, View view) {
        //获取管理者
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.removeView(view);
    }
}
