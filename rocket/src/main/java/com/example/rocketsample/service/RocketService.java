package com.example.rocketsample.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by huwang on 2017/4/4.
 */

public class RocketService  extends Service{
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("zhang", "RocketService创建");





    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("zhang", "RocketService销毁");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
