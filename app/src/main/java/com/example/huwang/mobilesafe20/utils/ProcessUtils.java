package com.example.huwang.mobilesafe20.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Debug;
import android.provider.Settings;
import android.util.Log;

import com.example.huwang.mobilesafe20.R;
import com.example.huwang.mobilesafe20.bean.ProcessInfo;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.R.id.list;
import static com.jaredrummler.android.processes.ProcessManager.getRunningAppProcesses;

/**
 * Created by huwang on 2017/4/6.
 */

public class ProcessUtils {
    /**
     * 获取进程总数
     *
     * @param context
     * @return
     */
    public static int getProcessCount(Context context) {
        // 进程管理者
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的进程
        List<AndroidAppProcess> processList = getRunningAppProcesses();

        return processList.size();
    }

    /**
     * 可用内存
     *
     * @param context
     * @return
     */
    public static long getAvailableMem(Context context) {
        // 进程管理者
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        // 获取内存信息
        am.getMemoryInfo(info);// byte
        return info.availMem;

    }

    /**
     * 总内存（高版本api）
     *
     * @param context
     * @return
     */
    @Deprecated
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static long getTotalMem(Context context) {
        // 进程管理者
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(info);// byte
        return info.totalMem;// 总内存 api 16 //高版本专有api 兼容性
    }

    /**
     * 字节
     *
     * @param context
     * @return
     */
    public static long getTotalMemFromProc(Context context) {
        long total = 0;
        try {
            File file = new File("proc/meminfo");

            // BufferedReader:字符读取器 readline();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            // 读取第一行
            String firstLine = reader.readLine();
            char[] chars = firstLine.toCharArray();
            // MemTotal: 599744 kB
            StringBuffer bf = new StringBuffer();
            for (char temp : chars) {
                if (temp >= '0' && temp <= '9') {
                    bf.append(temp);
                }
            }
            // 599744
            String result = bf.toString();
            long resultVlaue = Long.parseLong(result);
            total = resultVlaue * 1024;// kb--b
            reader.close();// 关闭
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;

    }

    /**
     * 获取系统内所有的进程信息
     *
     * @param context
     * @return
     */
    public static List<ProcessInfo> findAll(Context context) {
        List<ProcessInfo> list = new ArrayList<ProcessInfo>();
        // 获取进程管理者
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 正在运行
        List<AndroidAppProcess> processList = getRunningAppProcesses();
        //android5.0以下
//        List<ActivityManager.RunningAppProcessInfo> processList = am.getRunningAppProcesses();
//        Log.i("zhang", String.valueOf(processList.size()));
        // 包管理者
        PackageManager pm = context.getPackageManager();
        for (AndroidAppProcess process : processList) {
            // pid
            ProcessInfo bean = new ProcessInfo();
            bean.packageName = process.name;// 通常情况 包名=进程
            // --apk-->AndroidManifest.xml
            // 包名
            // PackageInfo:AndroidManifest.xml所有标签 activity service receiver...
            // pm.getPackageInfo(包名, 相关参数)
            try {
                PackageInfo pInfo = pm.getPackageInfo(bean.packageName, 0);

                // ApplicationInfo:指 application标签
                ApplicationInfo appInfo = pInfo.applicationInfo;
                // 图标
                bean.icon = appInfo.loadIcon(pm);
                // 文字
                bean.name = appInfo.loadLabel(pm).toString();
                // 内存
                // am.getProcessMemoryInfo(数组 pid 进程编号 );
                Debug.MemoryInfo[] meminfos = am.getProcessMemoryInfo(new int[]{process.pid});
                bean.memorySize = meminfos[0].getTotalPrivateDirty() * 1024;// 应用使用
                // 系统进程
                // flags 移位得到的类型相加
                if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    bean.isSystem = true;
                } else {
                    bean.isSystem = false;

                }
                // 添加到集合
                list.add(bean);

            } catch (PackageManager.NameNotFoundException e) {// 能常情况下 包名 :应用编号 == 进程编号
                e.printStackTrace();
                // 图标
                bean.icon = context.getResources().getDrawable(R.drawable.ic_launcher);
                // 名字
                bean.name = process.name;

                // am.getProcessMemoryInfo(数组 pid 进程编号 );
                Debug.MemoryInfo[] meminfos = am.getProcessMemoryInfo(new int[]{process.pid});
                bean.memorySize = meminfos[0].getTotalPrivateDirty() * 1024;// 应用使用
                // if(bean.name.contains("android")||bean.name.contains("system"))
                // {
                bean.isSystem = true;
                // }else
                // {
                // bean.isSystem=false;
                // }
                // 添加到集合
                list.add(bean);

            }// 0默认值 只解析 appliation标签以上


        }
        return list;
    }

    /**
     * 杀死进程
     *
     * @param context
     * @param processName
     */
    public static void killprocess(Context context, String processName) {
        // 进程管理
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 名称 3background 2forground
        am.killBackgroundProcesses(processName);
    }

    /**
     * 清理全部进程(backgorund以下)
     *
     * @param context
     */
    public static void killAll(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<AndroidAppProcess> list = getRunningAppProcesses();
        for (AndroidAppProcess info : list) {
            am.killBackgroundProcesses(info.name);
        }
    }

    /**
     * 获取用户正在操作的界面 对应的包名
     *
     * @param context
     * @return
     */
    public static String getCurrAppOnScreen(Context context) {
//        String packageName = "";
//        // 获取进程管理者
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        //需要权限【GET_TASK】
//        // RunningTaskInfo 栈的信息
//        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(Integer.MAX_VALUE);
////        List<ActivityManager.RecentTaskInfo> appTask = am.getRecentTasks(Integer.MAX_VALUE, 1);
//        // 获取栈Activity 即用户正在用
//        ComponentName topActivity = tasks.get(0).topActivity;
//        // 即将打开的
//        packageName = topActivity.getPackageName();
//
////        packageName = appTask.get(0).baseIntent.toString();
        String currentApp = "CurrentNULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        Log.i("zhang", "getCurrAppOnScreen--------" + currentApp);
        return currentApp;
    }

    public static boolean isNoSwitch(Context context) {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }
}
