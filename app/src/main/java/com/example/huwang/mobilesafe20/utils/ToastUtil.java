package com.example.huwang.mobilesafe20.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by huwang on 2017/3/19.
 */

public class ToastUtil {
    public static void shortToast(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void longToast(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
