package com.example.huwang.mobilesafe20.utils;

import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.RemoteException;
import android.util.Log;

import com.example.huwang.mobilesafe20.bean.AppInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by huwang on 2017/4/9.
 */

public class AppCacheUtil {

    /**
     * 读取缓存 data/data//cache
     *
     * @param context
     * @param info
     */
    public static void getAppCacheSize(Context context, final AppInfo info) {
        // GET_PACKAGE_SIZE
        // public void getPackageSizeInfo(String packageName,
        // IPackageStatsObserver observer) {
        // getPackageSizeInfo(packageName, UserHandle.myUserId(), observer);
        // }

        // 管理者
        PackageManager pm = context.getPackageManager();
        IPackageStatsObserver observer = new IPackageStatsObserver.Stub() {
            // 获取查询结果
            @Override
            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                Log.i("zhang", "succeeded " + succeeded + " :" + pStats.cacheSize);
                info.cacheSize = pStats.cacheSize;
            }
        };
        // cache 使用api
        try {
            Method getPackageSizeInfo = PackageManager.class.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            Log.i("zhang", getPackageSizeInfo.toString());

            getPackageSizeInfo.invoke(pm, info.packageName, observer);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // PackageManager}
    }

    /**
     * data/data//cache 只有系统的应用才能使用
     *
     * @param context
     * @param info
     */
    public static void deleteCacheFile(Context context, final AppInfo info) {
        // * @hide DELETE_CACHE_FILES
        // */
        // public abstract void deleteApplicationCacheFiles(String packageName,
        // IPackageDataObserver observer);
        IPackageDataObserver observer = new IPackageDataObserver.Stub() {
            @Override
            public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

                // 删除成功
                Log.i("zhang", "succeeded " + succeeded + " " + info.packageName);
            }
        };
        // 获取包管理者
        PackageManager pm = context.getPackageManager();
        // 类
        Class<?> clz = PackageManager.class;
        // 方法
        try {
            Method deleteApplicationCacheFiles = clz.getMethod("deleteApplicationCacheFiles", String.class, IPackageDataObserver.class);
            // 调用
            deleteApplicationCacheFiles.invoke(pm, info.packageName, observer);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 所有app的 data/data/包/cache
     *
     * @param context
     */
    public static void deleteAppsCacheFiles(Context context) {
        // * @hide
        // *CLEAN_APP_CACHE 权限
        // // @SystemApi
        // public abstract void freeStorageAndNotify(long freeStorageSize, 100M
        // 99M 101M不能低于实际被删除 的cache总大小
        // IPackageDataObserver observer);

        IPackageDataObserver observer = new IPackageDataObserver.Stub() {

            @Override
            public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

                Log.i("zhang", "onRemoveCompleted 成功");
            }
        };
        // 获得包管理者
        PackageManager pm = context.getPackageManager();

        // 类
        Class<?> clz = PackageManager.class;
        Method freeStorageAndNotify = null;
        //

        Method[] methods = clz.getDeclaredMethods();// public

        for (Method m : methods) {
            if (m.getName().equals("freeStorageAndNotify")) {
                freeStorageAndNotify = m;
                break;
            }
        }
        if (freeStorageAndNotify != null) {
            try {
                freeStorageAndNotify.invoke(pm, Long.MAX_VALUE, observer);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
