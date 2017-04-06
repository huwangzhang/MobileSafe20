package com.example.huwang.mobilesafe20.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.huwang.mobilesafe20.R;

/**
 * Created by huwang on 2017/4/4.
 */

public class ShortCutUtils {
    /**
     * 快捷方式
     *
     * @param context
     * @param activity
     */
    public static void addShortCut(Context context, Class<?> activity) {
        //只有隐式意图支持
        //
        // 1.Intent 激活程序
        Intent intent = new Intent();
        intent.setAction("ooo.aaa.bbb");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // 2.图标
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.samples_select);
        // 3.文字
        Intent broadcast = new Intent();
        broadcast.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        broadcast.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
        broadcast.putExtra(Intent.EXTRA_SHORTCUT_NAME, "黑卫");
        broadcast.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        broadcast.putExtra("duplicate", false);// 只允许一个快捷图标
        context.sendBroadcast(broadcast);

    }
}
