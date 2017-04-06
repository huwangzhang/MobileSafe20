package com.example.huwang.mobilesafe20.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.util.Log;

import com.example.huwang.mobilesafe20.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.filter;

/**
 * Created by huwang on 2017/4/4.
 */

public class AppUtils {
    /**
     *
     * @param context
     * @return
     */
    public static List<AppInfo> findAll(Context context) {
        // 创建集合
        List<AppInfo> list = new ArrayList<AppInfo>();
        // 包管理 所有已经安装的apk
        PackageManager pm = context.getPackageManager();
        // 所有已经安装的apk
        List<PackageInfo> pList = pm.getInstalledPackages(0);
        for (PackageInfo info : pList) {
            AppInfo bean = new AppInfo();
            bean.packageName = info.packageName;
            // ApplicationInfo application标签
            ApplicationInfo applicationInfo = info.applicationInfo;
            bean.path = applicationInfo.sourceDir;
            // 图标
            bean.icon = applicationInfo.loadIcon(pm);
            // 文字
            bean.name = applicationInfo.loadLabel(pm).toString();
            // 大小
            File apk = new File(applicationInfo.sourceDir);
//            bean.size = apk.getTotalSpace();
            bean.size = apk.length();// 总空间 Space--》目录

            //流量查询id
            bean.uid=applicationInfo.uid;

            //流量
            long mobileDownLoad= TrafficStats.getUidRxBytes(bean.uid);
            long mobileUpload=TrafficStats.getUidTxBytes(bean.uid);
            bean.mobileDownLoad=mobileDownLoad<0?0:mobileDownLoad;
            bean.mobileUpload=mobileUpload<0?0:mobileUpload;
            bean.mobileTotal=bean.mobileDownLoad+bean.mobileUpload;
            // 是否是系统权限
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                bean.isSys = true;
            } else {
                bean.isSys = false;
            }
            // 是否在手机内部
            if ((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
                bean.isInPhone = false;
            } else {
                bean.isInPhone = true;
            }

            list.add(bean);

        }
        return list;
    }

    /**
     * 显示详情
     *
     * @param info
     */
    public static void showApkDetail(Context context, AppInfo info) {
        // <action
        // android:name="android.settings.APPLICATION_DETAILS_SETTINGS"
        // />
        // <category android:name="android.intent.category.DEFAULT" />
        // <data android:scheme="package" />
        Intent setting = new Intent();
        setting.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");//
        setting.addCategory("android.intent.category.DEFAULT");
        // content://
        // file:///
        setting.setData(Uri.parse("package:" + info.packageName));
        context.startActivity(setting);
    }

    /**
     * 短信分享
     *
     * @param info
     */
    public static void shareApk(Context context, AppInfo info) {
        // <action android:name="android.intent.action.SEND" />
        // <category android:name="android.intent.category.DEFAULT" />
        // <data android:mimeType="text/plain" />

        Intent intent = new Intent();
        intent.setAction("android.intent.action.SENDTO");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sms:"));
//        intent.setType("text/plain");
        intent.putExtra("sms_body", "我在360手机助手里发现了个好东西【" + info.name + "】，快来 http://openbox.mobilem.360.cn/d.php?p=" + info.packageName + " 下载吧！360手机助手里还有很多好应用，来 http://mapp.360.cn/d 看看");

//        if (context == null) {
//            Log.i("zhang", "分享");
//            return;
//        }
        context.startActivity(intent);
    }

    /**
     * 启动
     *
     * @param info
     */
    public static void startApk(Context context, AppInfo info) {
        // 获取包管理器
        PackageManager pm = context.getPackageManager();
        // packageName ==>id
        Intent intent = pm.getLaunchIntentForPackage(info.packageName);
        Log.i("zhang", "启动程序");
        // Main LAUNCH
        if (intent == null) {
            ToastUtil.shortToast(context, "启动失败");
        } else {
            context.startActivity(intent);
        }
    }

    /**
     * 卸载 应用程序
     * @param context
     * @param info
     */
    public static void uninstallApk(Context context, AppInfo info) {
//        <intent-filter android:priority="1">
//                <action android:name="android.intent.action.DELETE"/>
//                <action android:name="android.intent.action.UNINSTALL_PACKAGE"/>
//                <category android:name="android.intent.category.DEFAULT"/>
//                <data android:scheme="package"/>
//            </intent-filter>


        // if (info != null && !info.isSys) {
         Intent intent = new Intent();
        intent.setAction("android.intent.action.UNINSTALL_PACKAGE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + info.packageName));
        context.startActivity(intent);
        // } else if (info != null && info.isSys) {
//        try {
//            // root 发送
//            // 1 授权
//            // 2.删除 应用
//            if (!RootTools.isAccessGiven()) {
//                ToastUtil.shortToast(context, "请授权黑马卫士超级管理员权限");
//                Log.i("zhang", "授权黑马卫士超级管理---");
//                return;
//            }
//            if (!RootTools.isRootAvailable()) {
//                ToastUtil.shortToast(context, "请授权黑马卫士超级管理员权限");
//                Log.i("zhang", "授权黑马卫士超级管理---");
//                return;
//            }
//            // 在项目中一定复制
//            RootTools.sendShell("mount -o remount  ,rw /system", 3000);
//            RootTools.sendShell("rm -r " + info.path, 3000);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // }
    }
}
