package com.example.adminmanager.utils;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;

import com.example.adminmanager.R;
import com.example.adminmanager.receiver.MyReceiver;

import static android.app.admin.DeviceAdminInfo.USES_POLICY_RESET_PASSWORD;
import static android.app.admin.DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY;

/**
 * Created by huwang on 2017/3/30.
 */

public class AdminUtils {
    /**
     * 锁屏 激活条件下调用
     *
     * @param act
     */
    public static void lockScreen(Activity act) {
        //设备权限管理
        DevicePolicyManager dpm = (DevicePolicyManager) act.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //锁屏
        //设置密码
//        ComponentName cn = dpm.getActiveAdmins().get(0);
//        Log.i("zhang","进入锁屏");
//        int i = dpm.getPasswordQuality(cn);
//        int j = dpm.getPasswordMinimumLength(cn);
//                dpm.setPasswordQuality(cn, 5);
//        dpm.setPasswordMinimumLength(cn, 5);
//        Log.i("zhang", "i=   " + i);
//        Log.i("zhang", "j=   " + j);
//        i = dpm.getPasswordQuality(cn);
//        Log.i("zhang", "i=   " + i);
//        if (dpm.resetPassword("2134", 0)) {
//            Log.i("zhang", "修改密码成功");
//        }
        dpm.resetPassword("1234", 0 );
        dpm.lockNow();

    }

    /**
     * 清楚数据
     *
     * @param context
     */
    public static void wipeData(Context context) {
        //设备权限管理
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //清除数据
        dpm.wipeData(0);//恢复出产设置
//        dpm.wipeData(DevicePolicyManager.WIPE_RESET_PROTECTION_DATA);
    }

    /**
     * 创建账号
     * @param act
     */
    public static void createAdmin(Activity act) {
        // Launch the activity to have the user enable our admin.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        //权限文件
        ComponentName cn = new ComponentName(act, MyReceiver.class);
        //EXTRA_DEVICE_ADMIN管理员账号
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "");
        act.startActivityForResult(intent, 0);
        Log.i("zhang", "超级管理员已被激活！");
    }

    /**
     * 删除账号
     * @param act
     */
    public static void descAdmin(Activity act) {
        ComponentName cn = new ComponentName(act, MyReceiver.class);
        DevicePolicyManager dpm = (DevicePolicyManager) act.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //删除账号
        dpm.removeActiveAdmin(cn);
        Log.i("zhang", "超级管理员已被删除!");
    }
}
