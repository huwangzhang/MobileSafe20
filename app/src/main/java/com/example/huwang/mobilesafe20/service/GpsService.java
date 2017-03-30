package com.example.huwang.mobilesafe20.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.huwang.mobilesafe20.utils.GpsUtils;

/**
 * Created by huwang on 2017/3/28.
 */

public class GpsService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("zhang", "定位服务创建GspService");
        GpsUtils.startLocation(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("zhang", "定位服务销毁GspService");
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
