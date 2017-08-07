package com.example.cachedemo;

import android.app.Activity;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void readcache(View view ){
        PackageManager pm = getPackageManager();
        IPackageStatsObserver observer = new IPackageStatsObserver.Stub(){
            //获取查询结果
            @Override
            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                Log.i("zhang", "succeeded " + succeeded + pStats.cacheSize);
            }
        };
//     public void getPackageSizeInfo(String packageName, IPackageStatsObserver observer) {
//        getPackageSizeInfoAsUser(packageName, UserHandle.myUserId(), observer);
        try {
            Method getPackageSizeInfo = PackageManager.class.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            Log.i("zhang", getPackageSizeInfo.toString());
            getPackageSizeInfo.invoke(pm, "com.netease.newsreader.activity", observer);


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    public void deletecache(View view ){

    }

    public void deleteall(View view ){

    }
}
