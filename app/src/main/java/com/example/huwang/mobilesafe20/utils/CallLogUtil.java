package com.example.huwang.mobilesafe20.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by huwang on 2017/3/30.
 */

public class CallLogUtil {
    /**
     * 删除 通话记录
     *
     * @param context
     * @param number
     */
    public static void deleteCallLogByNumber(Context context, String number) {

        // 访问地址 content://call_log/calls
//        Uri uri = Uri.parse("content://call_log/calls");
        // 获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        int result = resolver.delete(CallLog.Calls.CONTENT_URI, "number=?", new String[]{number});
        if (result > 0) {
            Log.i("zhang", "deleted success:" + number);
        } else {
            Log.i("zhang", "deleted fail:" + number);
        }
    }
}
