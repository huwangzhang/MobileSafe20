package com.example.adminmanager.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by huwang on 2017/3/30.
 */

public class MyReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.i("zhang", "创建超级管理员");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.i("zhang", "销毁超级管理员");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        Log.i("zhang", "密码改变");
    }
}
