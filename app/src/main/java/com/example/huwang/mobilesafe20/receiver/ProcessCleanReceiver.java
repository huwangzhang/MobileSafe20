package com.example.huwang.mobilesafe20.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.huwang.mobilesafe20.utils.ProcessUtils;
import com.example.huwang.mobilesafe20.utils.ToastUtil;

/**
 * Created by huwang on 2017/4/7.
 */

public class ProcessCleanReceiver extends BroadcastReceiver {
    // 接收到信号
    @Override
    public void onReceive(Context context, Intent intent) {
        ProcessUtils.killAll(context);
        ToastUtil.shortToast(context, "清理进程完毕!");
        Log.i("zhang", "--ScreenLockReceiver-锁屏清理--");
    }
}
