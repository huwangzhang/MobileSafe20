package com.example.huwang.mobilesafe20.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by huwang on 2017/4/4.
 */

public class WindowUtils {
    /**
     *
     * @param context
     * @param resId
     * @param x
     * @param y
     * @return
     */
    public static View showInWindow(Context context, int resId, int x, int y) {
        // 获取布局
        View view = View.inflate(context, resId, null);
        // 获取窗口管理者:添加视图对象到顶级布局
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // 宽 高 对齐 坐标 焦点 背景
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.format = PixelFormat.TRANSLUCENT;// 背景透明
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        // 叠加级别
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;// 吐司不支持拖动
        // 添加
        params.x = x;
        params.y = y;
        wm.addView(view, params);
        return view;
    }

    /**
     * 显示弹出视图
     * @param context
     * @param positionView
     * @param view
     * @param x
     * @param y
     * @return
     */
    public static PopupWindow showPopWindow(Context context, View positionView, View view, int x, int y) {

        // 创建 PopupWindow
        // PopupWindow popwindow=new PopupWindow(视图对象 , 宽, 高);
        PopupWindow popwindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //在视图对象 以外的范围 点击关闭
        popwindow.setOutsideTouchable(true);//自动关闭

        popwindow.setFocusable(true);//响应返回键关闭1.聚集 2.背景
        popwindow.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        // 指定控件做参考 显示出视图
        // popwindow.showAtLocation(参考视图, 坐标位置, 偏移x，偏移y)
        // int [] location =new int[2];
        // positionView.getLocationOnScreen(location);//获取视图在屏幕中的坐标
        popwindow.showAtLocation(positionView, Gravity.LEFT | Gravity.TOP, x, y);

        return popwindow;
    }
}
