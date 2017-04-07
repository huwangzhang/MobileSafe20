package com.example.huwang.mobilesafe20.service;


import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.huwang.mobilesafe20.receiver.ScreenLockReceiver;
import com.example.huwang.mobilesafe20.utils.ProcessUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by huwang on 2017/4/7.
 */

public class AutoKillProcessService extends Service {
    private Timer timer = null;
    private TimerTask task = null;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("zhang", "AutoKillProcessService:创建 ");
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Log.i("zhang", " --TimerTask run--"+Thread.currentThread().getName());
                ProcessUtils.killAll(getBaseContext());
            }
        };
        // timer.schedule(执行代码, 延后多久执行,时间间隔);
        // timer.schedule(task, 0,1*60*60*1000);上线
        timer.schedule(task, 0, 10 * 1000);

        //广播有两种注册方式
        //标签注册|代码注册
        //锁屏广播必须代码注册，标签注册不起作用
        receiver=new ScreenLockReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);//类型为锁屏广播
        registerReceiver(receiver, filter);
    }
    ScreenLockReceiver receiver;
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.i("zhang", "AutoKillProcessService:销毁 ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
