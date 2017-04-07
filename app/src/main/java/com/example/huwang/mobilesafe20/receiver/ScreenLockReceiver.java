package com.example.huwang.mobilesafe20.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.huwang.mobilesafe20.utils.ProcessUtils;

/**
 * Created by huwang on 2017/4/7.
 */

//1.标答注册
//2.代码注册
public class ScreenLockReceiver extends BroadcastReceiver {

    // 接收到信号
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("zhang", "--ScreenLockReceiver---");
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean lock_screen_cleanValue = sp.getBoolean("lock_screen_clean", false);
        if (lock_screen_cleanValue) { // 清理
            ProcessUtils.killAll(context);
            Log.i("zhang", "--ScreenLockReceiver-锁屏清理--");
        }

    }
}
